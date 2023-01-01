package com.epifoos.domain.player

import com.epifoos.domain.league.LeagueContext
import com.epifoos.domain.rank.PlayerRank
import com.epifoos.domain.stats.PlayerStats
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.and

object PlayerLeagueRepository {

    fun findById(playerId: Int, leagueContext: LeagueContext): PlayerWrapper? {
        val player = Player.find { PlayerTable.id eq playerId and (PlayerTable.league eq leagueContext.leagueId()) }
            .limit(1)
            .firstOrNull()
            ?.load(Player::user) ?: return null

        val stats = PlayerStats.findByPlayerAndSeason(playerId, leagueContext.seasonId())
        val rank = PlayerRank.findByPlayerAndSeason(playerId, leagueContext.seasonId())

        return PlayerWrapper(player, stats, rank)
    }

    fun findByUserId(userId: Int, leagueContext: LeagueContext): PlayerWrapper? {
        val player = Player.find { PlayerTable.user eq userId and (PlayerTable.league eq leagueContext.leagueId()) }
            .limit(1)
            .firstOrNull()
            ?.load(Player::user) ?: return null

        val stats = PlayerStats.findByPlayerAndSeason(player.id.value, leagueContext.seasonId())
        val rank = PlayerRank.findByPlayerAndSeason(player.id.value, leagueContext.seasonId())

        return PlayerWrapper(player, stats, rank)
    }

    fun findByIds(playerIds: Collection<Int>, leagueContext: LeagueContext): List<PlayerWrapper> {
        val players =
            Player.find { PlayerTable.id inList playerIds and (PlayerTable.league eq leagueContext.leagueId()) }
                .with(Player::user)

        val statsMap = PlayerStats.findByPlayersAndSeason(playerIds, leagueContext.seasonId())
            .associateBy { it.player }

        val rankMap = PlayerRank.findByPlayersAndSeason(playerIds, leagueContext.seasonId())
            .associateBy { it.player }

        return buildWrappers(players.toList(), statsMap, rankMap)
    }

    fun findAll(leagueContext: LeagueContext): List<PlayerWrapper> {
        val players = Player.find { PlayerTable.league eq leagueContext.leagueId() }
            .with(Player::user)

        val statsMap = PlayerStats.findByLeagueSeason(leagueContext.seasonId())
            .associateBy { it.player }

        val rankMap = PlayerRank.findByLeagueSeason(leagueContext.seasonId())
            .associateBy { it.player }

        return buildWrappers(players.toList(), statsMap, rankMap)
    }

    private fun buildWrappers(
        players: Collection<Player>,
        statsMap: Map<Player, PlayerStats>,
        rankMap: Map<Player, PlayerRank>
    ): List<PlayerWrapper> {
        return players.map { PlayerWrapper(it, statsMap[it], rankMap[it]) }
    }
}

data class PlayerWrapper(
    val player: Player,
    val stats: PlayerStats?,
    val rank: PlayerRank?
)

