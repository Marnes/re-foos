package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object StatsService {

    fun createDefault(league: League, player: Player) {
        PlayerStatsService.createDefault(league, player)
    }

    fun updateStats(calculationResult: CalculationResult) {
        PlayerStatsSnapshotService.createSnapshots(
            calculationResult.match,
            calculationResult.matchData.players.map { it.stats })

        PlayerStatsService.updateStats(calculationResult)
        MatchPlayerStatsService.createStats(calculationResult)
        MatchStatsService.createStats(calculationResult)
    }

    fun resetStats(league: League, players: List<Player>, matches: List<Match>, games: List<Game>) {
        val playerIds = players.map { it.id }
        val matchIds = matches.map { it.id }
        val gameIds = games.map { it.id }

        transaction {
            MatchPlayerStatsTable.deleteWhere { match inList matchIds }
            GamePlayerStatsTable.deleteWhere { player inList playerIds }
            MatchLosersTable.deleteWhere { player inList playerIds }
            MatchWinnersTable.deleteWhere { player inList playerIds }
            MatchStatsTable.deleteWhere { match inList matchIds }
            GameStatsTable.deleteWhere { game inList gameIds }
            PlayerStatsTable.deleteWhere { player inList playerIds }
            MatchPlayerStatsSnapshotTable.deleteWhere { player inList playerIds }
            players.forEach { createDefault(league, it) }
        }
    }
}
