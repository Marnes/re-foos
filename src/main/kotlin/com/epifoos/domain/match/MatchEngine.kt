package com.epifoos.domain.match

import com.epifoos.domain.calculation.CalculationService
import com.epifoos.domain.highlight.HighlightService
import com.epifoos.domain.league.League
import com.epifoos.domain.stats.StatsService

object MatchEngine {

    fun recalculate(league: League) {
        StatsService.resetStats(league)
        HighlightService.resetHighlights(league)

        //TODO: Should only be for league
        Match.all().forEach { doMagic(it) }
    }

    fun doMagic(match: Match) {
        val calculationResult = CalculationService.calculate(match)

        StatsService.updateStats(calculationResult)
        HighlightService.createHighlights(calculationResult)
    }
}
