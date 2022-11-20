package com.epifoos.domain.calculation.calculator.types

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.GameCalculationResult
import com.epifoos.domain.calculation.calculator.EloCalculator
import com.epifoos.domain.calculation.coefficient.dto.GameCoefficients
import com.epifoos.domain.calculation.coefficient.dto.MatchCoefficients
import com.epifoos.domain.calculation.coefficient.types.RoundRobinCoefficientCalculator
import com.epifoos.domain.calculation.data.types.RoundRobinDataMapper
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

class RoundRobinCalculator :
    EloCalculator<RoundRobinCoefficientCalculator, RoundRobinDataMapper>() {

    companion object {
        private const val RESULT_WEIGHT = 0.5F
        private const val SCORE_WEIGHT = 1.0F - RESULT_WEIGHT
        private const val K_VALUE = 36.0F

        fun create(): RoundRobinCalculator {
            return RoundRobinCalculator()
        }
    }

    override fun calculate(
        match: Match,
    ): CalculationResult {
        val matchData = dataMapper.getMatchData(match)
        val matchCoefficients = coefficientCalculator.calculateCoefficient(match, matchData)
        val gameResults = calculateGameResults(match, matchData.players, matchCoefficients)
        val eloChanges = calculateTotalScores(gameResults)

        return CalculationResult(
            match,
            matchData,
            matchCoefficients,
            eloChanges,
            gameResults
        )
    }

    private fun calculateGameResults(
        match: Match,
        players: Set<Player>,
        matchCoefficients: MatchCoefficients,
    ): List<GameCalculationResult> {
        return match.games.map { GameCalculationResult(it, calculate(players, matchCoefficients.getCoefficient(it))) }
    }

    private fun calculate(players: Set<Player>, coefficients: GameCoefficients): Map<Player, Float> {
        val scoreBasedEloChanges = calculateScoreBasedEloChanges(players, coefficients)
        val resultBasedEloChanges = calculateResultBasedEloChanges(players, coefficients)

        return (scoreBasedEloChanges.keys + resultBasedEloChanges.keys)
            .associateWith { calculateOverallEloChanges(resultBasedEloChanges[it]!!, scoreBasedEloChanges[it]!!) }
    }

    private fun calculateScoreBasedEloChanges(
        players: Set<Player>,
        coefficients: GameCoefficients
    ): Map<Player, Float> {
        return players.associateWith {
            calculateEloChange(
                coefficients.getActualScoreCoefficient(it),
                coefficients.getExpectedScoreCoefficient(it)
            )
        }
    }

    private fun calculateResultBasedEloChanges(
        players: Set<Player>,
        coefficients: GameCoefficients
    ): Map<Player, Float> {
        return players.associateWith {
            calculateEloChange(
                coefficients.getResultScoreCoefficient(it),
                coefficients.getExpectedScoreCoefficient(it)
            )
        }
    }

    private fun calculateOverallEloChanges(resultBasedEloChange: Float, scoreBasedEloChange: Float): Float {
        return (resultBasedEloChange * RESULT_WEIGHT + scoreBasedEloChange * SCORE_WEIGHT)
    }

    private fun calculateEloChange(actualScore: Float, expectedScore: Float): Float {
        return K_VALUE * (actualScore - expectedScore)
    }
}
