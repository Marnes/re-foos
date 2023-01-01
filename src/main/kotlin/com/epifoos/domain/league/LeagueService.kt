package com.epifoos.domain.league

import com.epifoos.domain.league.builder.LeagueBuilderService
import com.epifoos.domain.league.dto.*
import com.epifoos.domain.league.validation.LeagueValidationService
import com.epifoos.domain.player.PlayerService
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.user.User
import com.epifoos.exceptions.AuthorizationException
import com.epifoos.exceptions.BaseException
import com.epifoos.exceptions.EntityNotFoundException
import com.epifoos.exceptions.ValidationException
import io.konform.validation.Invalid
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

        transaction { LeagueBuilderService.create(leagueCreationDto, currentUser) }

        return leagueCreationDto
    }

    fun getLeague(leagueContext: LeagueContext, currentUser: User?): LeagueDto {
        return transaction {
            LeagueDtoMapper.map(
                leagueContext.league,
                getUserLeagueIds(currentUser).contains(leagueContext.league.id.value)
            )
        }
    }

    fun getLeagues(currentUser: User?): List<LeagueSummaryDto> {
        val userLeagueIds = transaction { getUserLeagueIds(currentUser) }

        return transaction {
            League.all()
                .with(League::config, League::createdBy)
                .toList()
                .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
                .map { LeagueDtoMapper.mapSummary(it, userLeagueIds.contains(it.id.value)) }
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
            val uid = LeagueUtil.generateUid()

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

            if (getUserLeagueIds(currentUser).contains(league.id.value)) {
                throw BaseException("User already joined league")
            }

            if (league.isClosed()) {
                throw BaseException("League is closed");
            }

            PlayerService.createPlayer(league, currentUser)
        }
    }

    private fun getUserLeagueIds(currentUser: User?): List<Int> {
        if (currentUser == null) {
            return listOf()
        }

        return PlayerTable
            .slice(PlayerTable.league)
            .select { PlayerTable.user eq currentUser.id }
            .map { it[PlayerTable.league].value }
    }

    private fun leagueNotFound(leagueId: Int): EntityNotFoundException {
        return EntityNotFoundException("Could not find league with ID $leagueId")
    }
}
