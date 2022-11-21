package com.epifoos.domain.player

import com.epifoos.domain.league.League
import com.epifoos.domain.match.MatchService
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.dto.PlayerDto
import com.epifoos.domain.player.dto.PlayerDtoMapper
import com.epifoos.domain.player.dto.PlayerSpotlightDto
import com.epifoos.domain.stats.MatchPlayerStatsTable
import com.epifoos.domain.stats.StatsService
import com.epifoos.domain.user.User
import org.jetbrains.exposed.dao.with
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

            StatsService.createDefault(league, player)
            player
        }
    }

    fun getPlayers(leagueId: Int): List<PlayerDto> {
        return transaction {
            Player.find { PlayerTable.league eq leagueId }
                .with(Player::user)
                .with(Player::stats)
                .map { PlayerDtoMapper.map(it, it.stats) }
        }
    }

    fun getPlayerSpotlight(leagueId: Int): PlayerSpotlightDto {
        return transaction {
            val player = PlayerSpotlightService.getSpotlight(leagueId)
            val matchId = (MatchTable innerJoin MatchPlayerStatsTable)
                .slice(MatchTable.id)
                .select { MatchPlayerStatsTable.player eq player.id }
                .orderBy(MatchTable.createdDate to SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            PlayerSpotlightDto(
                PlayerDtoMapper.map(player, player.stats),
                if (matchId == null) null else MatchService.getMatch(matchId[MatchTable.id].value)
            )
        }
    }
}
