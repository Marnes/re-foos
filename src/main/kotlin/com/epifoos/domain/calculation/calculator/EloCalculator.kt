package com.epifoos.domain.calculation.calculator

import com.epifoos.domain.Elo
import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.GameCalculationResult
import com.epifoos.domain.calculation.coefficient.CoefficientCalculator
import com.epifoos.domain.calculation.data.MatchDataMapper
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player
import com.epifoos.domain.sum

abstract class EloCalculator<C : CoefficientCalculator, D : MatchDataMapper<*>>(
    val dataMapper: D,
    val coefficientCalculator: C
) {

    abstract fun calculate(
        match: Match,
        players: Set<Player>,
        initialEloMap: Map<Player, Elo>
    ): CalculationResult

    protected fun calculateTotalScores(gameResults: List<GameCalculationResult>): Map<Player, Elo> {
        return gameResults.flatMap { it.eloChanges.asSequence() }
            .groupBy { it.key }
            .mapValues { (_, eloChanges) -> eloChanges.map { it.value }.sum() }
    }
}
