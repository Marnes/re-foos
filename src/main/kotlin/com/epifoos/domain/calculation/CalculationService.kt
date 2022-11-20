package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.calculator.types.RoundRobinCalculator
import com.epifoos.domain.calculation.coefficient.types.RoundRobinCoefficientCalculator
import com.epifoos.domain.calculation.data.win.DefaultMatchDataMapper
import com.epifoos.domain.calculation.data.win.RoundRobinWinConditionMapper
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match
import com.epifoos.domain.stats.StatsService
import org.jetbrains.exposed.sql.transactions.transaction

object CalculationService {

    fun recalculate(league: League) {
        transaction {
            StatsService.resetStats(league)
            calculate( Match.all().minBy { it.createdDate })
        }
    }

    fun calculate(match: Match) {
        val calculationResult = RoundRobinCalculator
            .create()
            .setDataMapper(DefaultMatchDataMapper(RoundRobinWinConditionMapper()))
            .setCoefficientCalculator(RoundRobinCoefficientCalculator())
            .calculate(match)

        StatsService.updateStats(calculationResult)
    }
}
