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
import com.epifoos.exceptions.EntityNotFoundException
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
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

    fun getPlayer(user: User, leagueId: Int): PlayerDto? {
        return transaction {
            val player = Player.find { PlayerTable.user eq user.id and (PlayerTable.league eq leagueId) }
                .limit(1)
                .firstOrNull()
                ?.load(Player::user, Player::rank, Player::stats)

            player?.let { PlayerDtoMapper.map(it, it.rank, it.stats) }
        }
    }

    fun getPlayers(leagueId: Int): List<PlayerDto> {
        return transaction {
            Player.find { PlayerTable.league eq leagueId }
                .with(Player::stats, Player::rank, Player::user)
                .map { PlayerDtoMapper.map(it, it.rank, it.stats) }
        }
    }

    fun getPlayerSpotlight(leagueId: Int, playerId: Int): PlayerSpotlightDto {
        return transaction {
            val player = Player.find { PlayerTable.league eq leagueId and (PlayerTable.id eq playerId) }.firstOrNull()
                ?.load(Player::stats, Player::user, Player::rank)
                ?: throw EntityNotFoundException("Could not find a Player for League $leagueId")

            val matchId = (MatchTable innerJoin MatchPlayerStatsTable)
                .slice(MatchTable.id)
                .select { MatchPlayerStatsTable.player eq player.id }
                .orderBy(MatchTable.createdDate to SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            PlayerSpotlightDto(
                PlayerDtoMapper.map(player, player.rank, player.stats),
                if (matchId == null) null else MatchService.getMatch(matchId[MatchTable.id].value)
            )
        }
    }

    fun getOrUpdateSpotlight(leagueId: Int): PlayerDto {
        return transaction {
            val player = PlayerSpotlightService.getSpotlight(leagueId)
            PlayerDtoMapper.map(player, player.rank, player.stats)
        }
    }
}
