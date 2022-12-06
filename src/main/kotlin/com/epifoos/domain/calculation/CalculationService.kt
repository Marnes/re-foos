package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.calculator.types.RoundRobinCalculator
import com.epifoos.domain.calculation.coefficient.types.RoundRobinCoefficientCalculator
import com.epifoos.domain.calculation.data.win.DefaultMatchDataMapper
import com.epifoos.domain.calculation.data.win.RoundRobinWinConditionMapper
import com.epifoos.domain.match.Match

object CalculationService {

    fun calculate(match: Match): CalculationResult {
        return RoundRobinCalculator
            .create()
            .setDataMapper(DefaultMatchDataMapper(RoundRobinWinConditionMapper()))
            .setCoefficientCalculator(RoundRobinCoefficientCalculator())
            .calculate(match)
    }
}
