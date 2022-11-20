package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.calculator.types.RoundRobinCalculator
import com.epifoos.domain.calculation.coefficient.types.RoundRobinCoefficientCalculator
import com.epifoos.domain.calculation.data.types.RoundRobinDataMapper
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match
import com.epifoos.domain.stats.StatsService
import org.jetbrains.exposed.sql.transactions.transaction

object CalculationService {

    fun recalculate(league: League) {
        transaction {
            StatsService.resetStats(league)
            Match.all().sortedBy { it.createdDate }.forEach { calculate(it) }
        }
    }

    fun calculate(match: Match) {
        val calculationResult = RoundRobinCalculator
            .create()
            .setDataMapper(RoundRobinDataMapper.create())
            .setCoefficientCalculator(RoundRobinCoefficientCalculator.create())
            .calculate(match)

        StatsService.updateStats(calculationResult)
    }
}
