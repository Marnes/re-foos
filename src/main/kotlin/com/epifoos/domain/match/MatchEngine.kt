package com.epifoos.domain.match

import com.epifoos.domain.calculation.CalculationService
import com.epifoos.domain.highlight.HighlightService
import com.epifoos.domain.league.League
import com.epifoos.domain.rank.PlayerRankService
import com.epifoos.domain.stats.StatsService
import org.jetbrains.exposed.sql.transactions.transaction

object MatchEngine {

    fun calculate(league: League, match: Match) {
        transaction {
            val calculationResult = CalculationService.calculate(league, match)

            StatsService.updateStats(calculationResult)
            HighlightService.createHighlights(calculationResult)
            PlayerRankService.updateRanks(league, match)
        }
    }
}
