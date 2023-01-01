package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.league.LeagueSeason
import com.epifoos.domain.match.MatchCalculationSubmission
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

object StatsService {

    fun updateStats(matchCalculationSubmission: MatchCalculationSubmission, calculationResult: CalculationResult) {
        PlayerStatsSnapshotService.createSnapshots(
            calculationResult.match,
            matchCalculationSubmission.getStats()
        )

        PlayerStatsService.updateStats(
            matchCalculationSubmission.leagueContext,
            matchCalculationSubmission.getPlayers(),
            matchCalculationSubmission.getStatsMap(),
            calculationResult
        )

        MatchPlayerStatsService.createStats(calculationResult)
        MatchStatsService.createStats(calculationResult)
    }

    fun resetStats(playerIds: List<Int>, matchIds: List<Int>, gameIds: List<Int>, leagueSeason: LeagueSeason) {
        val matchStatsIds = MatchStats.find { MatchStatsTable.match inList matchIds}.map { it.id }

        MatchPlayerStatsTable.deleteWhere { match inList matchIds }
        GamePlayerStatsTable.deleteWhere { game inList gameIds }
        MatchLosersTable.deleteWhere { matchStats inList matchStatsIds }
        MatchWinnersTable.deleteWhere { matchStats inList matchStatsIds }
        MatchStatsTable.deleteWhere { match inList matchIds }
        GameStatsTable.deleteWhere { game inList gameIds }
        PlayerStatsTable.deleteWhere { player inList playerIds and (season eq leagueSeason.id) }
        MatchPlayerStatsSnapshotTable.deleteWhere { match inList matchIds }
    }
}
