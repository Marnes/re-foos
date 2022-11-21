package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.league.League
import com.epifoos.domain.player.Player
import org.jetbrains.exposed.sql.deleteAll
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

    fun resetStats(league: League) {
        transaction {
            //TODO: Only delete for league
            MatchPlayerStatsTable.deleteAll()
            GamePlayerStatsTable.deleteAll()
            MatchStatsTable.deleteAll()
            GameStatsTable.deleteAll()
            PlayerStatsTable.deleteAll()
            PlayerStatsSnapshotTable.deleteAll()
            Player.all().forEach { createDefault(league, it) }
        }
    }
}
