package com.epifoos.domain.player

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueContext
import com.epifoos.domain.match.MatchService
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.dto.PlayerDto
import com.epifoos.domain.player.dto.PlayerDtoMapper
import com.epifoos.domain.player.dto.PlayerSpotlightDto
import com.epifoos.domain.stats.MatchPlayerStatsTable
import com.epifoos.domain.user.User
import com.epifoos.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerService {

    fun createPlayer(league: League, user: User): Player {
        return transaction {
            val player = Player.new {
                this.user = user
                this.league = league
            }

            player
        }
    }

    fun getPlayer(leagueContext: LeagueContext, currentUser: User): PlayerDto? {
        return transaction {
            val player = PlayerLeagueRepository.findByUserId(currentUser.id.value, leagueContext)

            player?.let { PlayerDtoMapper.map(it.player, it.rank, it.stats) }
        }
    }

    fun getPlayers(leagueContext: LeagueContext): List<PlayerDto> {
        return transaction {
            PlayerLeagueRepository.findAll(leagueContext)
                .map { PlayerDtoMapper.map(it.player, it.rank, it.stats) }
        }
    }

    fun getPlayerSpotlight(leagueContext: LeagueContext, playerId: Int): PlayerSpotlightDto {
        return transaction {
            val playerWrapper = PlayerLeagueRepository.findById(playerId, leagueContext)
                ?: throw EntityNotFoundException("Could not find a Player for League ${leagueContext.leagueId()}")

            val matchId = (MatchTable innerJoin MatchPlayerStatsTable)
                .slice(MatchTable.id)
                .select { MatchPlayerStatsTable.player eq playerWrapper.player.id }
                .orderBy(MatchTable.createdDate to SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            PlayerSpotlightDto(
                PlayerDtoMapper.map(playerWrapper.player, playerWrapper.rank, playerWrapper.stats),
                matchId?.let { MatchService.getMatch(it[MatchTable.id].value) }
            )
        }
    }

}
