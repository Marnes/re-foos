package com.epifoos.domain.calculation.calculator.types

import com.epifoos.domain.Elo
import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.GameCalculationResult
import com.epifoos.domain.calculation.calculator.EloCalculator
import com.epifoos.domain.calculation.coefficient.GameCoefficients
import com.epifoos.domain.calculation.coefficient.MatchCoefficients
import com.epifoos.domain.calculation.coefficient.types.TeamBasedCoefficientCalculator
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.calculation.data.win.TeamBasedMatchDataMapper
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

class HeadToHeadCalculator(dataMapper: TeamBasedMatchDataMapper<*>, coefficientCalculator: TeamBasedCoefficientCalculator) :
    EloCalculator<TeamBasedCoefficientCalculator, TeamBasedMatchDataMapper<*>>(dataMapper, coefficientCalculator) {

    companion object {
        private const val RESULT_WEIGHT = 0.5F
        private const val SCORE_WEIGHT = 1.0F - RESULT_WEIGHT
        private const val K_VALUE = 36.0F
    }

    override fun calculate(
        league: League,
        match: Match,
        players: Set<Player>,
        initialEloMap: Map<Player, Elo>
    ): CalculationResult {
        val matchData = dataMapper.getMatchData(league, match, players, initialEloMap)
        val matchCoefficients = coefficientCalculator.calculateCoefficient(match, matchData)
        val gameResults = calculateGameResults(match, matchData, matchCoefficients)
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
        matchData: MatchData,
        matchCoefficients: MatchCoefficients,
    ): List<GameCalculationResult> {
        return match.games.map {
            GameCalculationResult(
                it,
                matchData.gameDataMap[it]!!,
                calculate(matchData.players, matchCoefficients.getCoefficient(it))
            )
        }
    }

    private fun calculate(players: Set<Player>, coefficients: GameCoefficients): Map<Player, Elo> {
        val scoreBasedEloChanges = calculateScoreBasedEloChanges(players, coefficients)
        val resultBasedEloChanges = calculateResultBasedEloChanges(players, coefficients)

        return (scoreBasedEloChanges.keys + resultBasedEloChanges.keys)
            .associateWith { calculateOverallEloChanges(resultBasedEloChanges[it]!!, scoreBasedEloChanges[it]!!) }
    }

    private fun calculateScoreBasedEloChanges(
        players: Set<Player>,
        coefficients: GameCoefficients
    ): Map<Player, Elo> {
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
    ): Map<Player, Elo> {
        return players.associateWith {
            calculateEloChange(
                coefficients.getResultScoreCoefficient(it),
                coefficients.getExpectedScoreCoefficient(it)
            )
        }
    }

    private fun calculateEloChange(actualScore: Double, expectedScore: Double): Elo {
        return Elo(K_VALUE * (actualScore - expectedScore))
    }

    private fun calculateOverallEloChanges(resultBasedEloChange: Elo, scoreBasedEloChange: Elo): Elo {
        return Elo(resultBasedEloChange * RESULT_WEIGHT + scoreBasedEloChange * SCORE_WEIGHT)
    }
}
