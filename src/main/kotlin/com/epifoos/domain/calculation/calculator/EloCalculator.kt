package com.epifoos.domain.calculation.calculator

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.GameCalculationResult
import com.epifoos.domain.calculation.coefficient.CoefficientCalculator
import com.epifoos.domain.calculation.data.DataMapper
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

abstract class EloCalculator<C : CoefficientCalculator, D : DataMapper> {
    lateinit var dataMapper: D
    lateinit var coefficientCalculator: C

    abstract fun calculate(match: Match): CalculationResult

    fun setDataMapper(dataMapper: D): EloCalculator<C, D> {
        this.dataMapper = dataMapper
        return this
    }

    fun setCoefficientCalculator(coefficientCalculator: C): EloCalculator<C, D> {
        this.coefficientCalculator = coefficientCalculator
        return this
    }

    protected fun calculateTotalScores(gameResults: List<GameCalculationResult>): Map<Player, Float> {
        return gameResults.flatMap { it.eloChanges.asSequence() }
            .groupBy { it.key }
            .mapValues { (_, eloChanges) -> eloChanges.map { it.value }.sum() }
    }
}
