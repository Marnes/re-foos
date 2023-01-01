package com.epifoos.domain.match

import com.epifoos.domain.calculation.CalculationService
import com.epifoos.domain.highlight.HighlightService
import com.epifoos.domain.rank.PlayerRankService
import com.epifoos.domain.stats.StatsService

object MatchEngine {

    fun calculate(matchCalculationSubmission: MatchCalculationSubmission) {
        val calculationResult = CalculationService.calculate(matchCalculationSubmission)

        StatsService.updateStats(matchCalculationSubmission, calculationResult)
        HighlightService.createHighlights(matchCalculationSubmission.leagueContext.league, calculationResult)
        PlayerRankService.updateRanks(matchCalculationSubmission)
    }
}
