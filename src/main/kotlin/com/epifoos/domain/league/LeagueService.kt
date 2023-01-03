package com.epifoos.domain.league

import com.epifoos.domain.league.builder.LeagueBuilderService
import com.epifoos.domain.league.dto.LeagueCode
import com.epifoos.domain.league.dto.LeagueCreationDto
import com.epifoos.domain.league.dto.LeagueDto
import com.epifoos.domain.league.dto.LeagueDtoMapper
import com.epifoos.domain.league.validation.LeagueValidationService
import com.epifoos.domain.match.MatchService
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.PlayerService
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.user.User
import com.epifoos.exceptions.AuthorizationException
import com.epifoos.exceptions.BaseException
import com.epifoos.exceptions.EntityNotFoundException
import com.epifoos.exceptions.ValidationException
import io.konform.validation.Invalid
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object LeagueService {

    fun createLeague(leagueCreationDto: LeagueCreationDto, currentUser: User): LeagueCreationDto {
        val validationResult = LeagueValidationService.validate(leagueCreationDto)

        if (validationResult is Invalid) {
            throw ValidationException(validationResult.errors)
        }

        transaction {
            LeagueBuilderService.create(leagueCreationDto, currentUser)
                .also {
                    LeagueSeason.new {
                        league = it
                        startDate = LocalDate.now()
                        season = 1
                        createdBy = currentUser
                    }
                }
                .also {
                    LeagueCoefficients.new {
                        league = it
                        kValue = leagueCreationDto.coefficients.kValue
                        resultCoefficient = leagueCreationDto.coefficients.resultCoefficient
                    }
                }
        }

        return leagueCreationDto
    }

    //TODO: Less expensive call for just capturing with config
    fun getLeague(leagueContext: LeagueContext, currentUser: User?): LeagueDto {
        return transaction {
            val joined = LeagueHelper.getUserLeagueIds(currentUser).contains(leagueContext.league.id.value)
            leagueContext.league.load(League::config, League::createdBy, League::seasons)

            LeagueDtoMapper.map(
                leagueContext.league,
                leagueContext.season,
                LeagueHelper.getLeagueLeader(leagueContext.season),
                PlayerTable.select { PlayerTable.league eq leagueContext.league.id }.count(),
                MatchTable.select { MatchTable.season eq leagueContext.season.id }.count(),
                joined,
                leagueContext.league.getActiveOrLastSeason().isOpen(),
                LeagueHelper.getLatestMatchId(leagueContext)?.let { MatchService.getMatch(it) }
            )
        }
    }

    fun getLeagues(currentUser: User?): List<LeagueDto> {
        return transaction {
            val userLeagueIds = LeagueHelper.getUserLeagueIds(currentUser)

            val leagues = League.all()
                .with(League::config, League::createdBy, League::seasons)
                .toList()

            val seasonsMap = leagues.associateWith { it.getActiveOrLastSeason() }
            val leadersMap = LeagueHelper.getLeaguesLeaders(seasonsMap.values)

            leagues.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
                .map {
                    val joined = userLeagueIds.contains(it.id.value)
                    val season = seasonsMap[it]!!

                    LeagueDtoMapper.map(
                        it,
                        season,
                        leadersMap[season],
                        PlayerTable.select { PlayerTable.league eq it.id }.count(), //TODO: More Performant
                        MatchTable.select { MatchTable.season eq season.id }.count(), //TODO: More Performant
                        joined,
                        season.isOpen(),
                        null
                    )
                }
        }
    }

    fun getLeagueCode(leagueId: Int): LeagueCode {
        return transaction {
            League.findById(leagueId)?.uid?.let { LeagueCode(it) } ?: throw leagueNotFound(leagueId)
        }
    }

    fun generateNewCode(leagueId: Int): LeagueCode {
        return transaction {
            val league = League.findById(leagueId) ?: throw leagueNotFound(leagueId)
            val uid = LeagueHelper.generateUid()

            league.uid = uid
            LeagueCode(uid)
        }
    }

    fun setLeagueEndDate(leagueId: Int, endDate: LocalDate) {
        transaction {
            val league = League.findById(leagueId) ?: throw leagueNotFound(leagueId)
        }
    }

    fun joinLeague(leagueId: Int, currentUser: User, code: String) {
        transaction {
            val league = League.findById(leagueId) ?: throw leagueNotFound(leagueId)

            if (league.uid != code) {
                throw AuthorizationException("Invalid code supplied")
            }

            if (LeagueHelper.getUserLeagueIds(currentUser).contains(league.id.value)) {
                throw BaseException("User already joined league")
            }

            PlayerService.createPlayer(league, currentUser)
        }
    }

    private fun leagueNotFound(leagueId: Int): EntityNotFoundException {
        return EntityNotFoundException("Could not find league with ID $leagueId")
    }
}
