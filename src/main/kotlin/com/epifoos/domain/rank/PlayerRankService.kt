package com.epifoos.domain.rank

import com.epifoos.domain.Elo
import com.epifoos.domain.league.LeagueContext
import com.epifoos.domain.league.LeagueSeason
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchCalculationSubmission
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerLeagueRepository
import com.epifoos.domain.player.PlayerWrapper
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

object PlayerRankService {

    fun updateRanks(matchCalculationSubmission: MatchCalculationSubmission) {
        val playerWrappers =
            PlayerLeagueRepository.findAll(matchCalculationSubmission.leagueContext).filter { it.stats != null }
                .sortedByDescending { it.stats!!.elo }

        saveSnapshot(playerWrappers, matchCalculationSubmission.match)
        rankPlayers(playerWrappers, matchCalculationSubmission.leagueContext)
    }

    fun resetRanks(playerIds: List<Int>, matchIds: List<Int>, leagueSeason: LeagueSeason) {
        PlayerRankTable.deleteWhere { player inList playerIds and (season eq leagueSeason.id) }
        MatchPlayerRankSnapshotTable.deleteWhere { match inList matchIds }
    }

    fun getRankSnapshots(match: Match): List<MatchPlayerRankSnapshot> {
        return MatchPlayerRankSnapshot.find { MatchPlayerRankSnapshotTable.match eq match.id }
            .with(MatchPlayerRankSnapshot::player).toList()
    }

    private fun saveSnapshot(leaguePlayers: List<PlayerWrapper>, match: Match) {
        leaguePlayers.filter { it.rank != null }.map { it.rank!! }.forEach { createSnapshot(it, match) }
    }

    private fun rankPlayers(leaguePlayers: List<PlayerWrapper>, leagueContext: LeagueContext) {
        var currentRank = 0
        var currentElo: Elo? = null

        val playerRankMap = leaguePlayers.associateBy({ it.player }, { it.rank })

        leaguePlayers.filter { it.stats != null }.forEach {
            if (currentElo == null || currentElo != it.stats!!.elo) {
                currentRank++
            }

            updatePlayerRank(currentRank, it.player, leagueContext, playerRankMap)
            currentElo = it.stats!!.elo
        }
    }

    private fun updatePlayerRank(
        rank: Int, player: Player, leagueContext: LeagueContext, playerRankMap: Map<Player, PlayerRank?>
    ) {
        val playerRank = playerRankMap[player] ?: PlayerRank.new {
            this.player = player
            this.league = leagueContext.league
            this.season = leagueContext.season
        }
        playerRank.rank = rank
    }

    private fun createSnapshot(playerRank: PlayerRank, match: Match) {
        MatchPlayerRankSnapshot.new {
            this.match = match
            player = playerRank.player
            rank = playerRank.rank
        }
    }
}
