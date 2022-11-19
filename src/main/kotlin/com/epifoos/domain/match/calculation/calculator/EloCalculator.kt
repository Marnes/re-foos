package com.epifoos.domain.match.calculation.calculator

import com.epifoos.domain.match.calculation.mapper.MatchMapper
import com.epifoos.match.calculation.CalculationResult

abstract class EloCalculator(val matchMapper: MatchMapper) {

    companion object {
        private const val K_VALUE = 36.0F
    }

    abstract fun calculate(): CalculationResult;

    protected fun calculateEloChange(actualScore: Float, expectedScore: Float): Float {
        return K_VALUE * (actualScore - expectedScore)
    }
}
