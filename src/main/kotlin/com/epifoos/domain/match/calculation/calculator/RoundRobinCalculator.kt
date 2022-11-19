package com.epifoos.domain.match.calculation.calculator

import com.epifoos.domain.match.calculation.mapper.GameMapper
import com.epifoos.domain.match.calculation.mapper.MatchMapper
import com.epifoos.domain.player.Player
import com.epifoos.match.calculation.CalculationResult
import com.epifoos.match.calculation.GameCalculationResult

class RoundRobinCalculator(matchMapper: MatchMapper) : EloCalculator(matchMapper) {

    companion object {
        private const val STARTING_CHANGE = 0.0F
        private const val ELO_WEIGHT = 400.0F

        private const val MAX_SCORE = 10

        private const val RESULT_WEIGHT = 0.5F
        private const val GOAL_WEIGHT = 1.0F - RESULT_WEIGHT

        fun create(matchMapper: MatchMapper): RoundRobinCalculator {
            return RoundRobinCalculator(matchMapper)
        }
    }

    override fun calculate(): CalculationResult {
        val gameResults = matchMapper.gameMappers.map { calculateGameResult(it) }

        return CalculationResult(calculateTotalScores(gameResults), gameResults)
    }


    private fun calculateTotalScores(gameResults: List<GameCalculationResult>): Map<Player, Float> {
        return gameResults.flatMap { it.eloChanges.asSequence() }
            .groupBy { it.key }
            .mapValues { (_, eloChanges) -> eloChanges.map { it.value }.sum() }
    }

    private fun calculateGameResult(gameMapper: GameMapper): GameCalculationResult {
        return GameCalculationResult(gameMapper.game, calculateGameEloChanges(gameMapper))
    }

    private fun calculateGameEloChanges(gameMapper: GameMapper): Map<Player, Float> {
        val scoreBasedEloChanges = calculateScoreBasedEloChanges(gameMapper)
        val resultBasedEloChanges = calculateResultBasedEloChanges(gameMapper)

        return (scoreBasedEloChanges.keys + resultBasedEloChanges.keys)
            .associateWith { calculateOverallEloChanges(resultBasedEloChanges[it]!!, scoreBasedEloChanges[it]!!) }
    }

    private fun calculateScoreBasedEloChanges(gameMapper: GameMapper): Map<Player, Float> {
        return matchMapper.players
            .associateWith { calculateEloChange(gameMapper.getActualScore(it), gameMapper.getExpectedScore(it)) }
    }

    private fun calculateResultBasedEloChanges(gameMapper: GameMapper): Map<Player, Float> {
        return matchMapper.players
            .associateWith { calculateEloChange(gameMapper.getResult(it).weight, gameMapper.getExpectedScore(it)) }
    }

    private fun calculateOverallEloChanges(resultBasedEloChange: Float, scoreBasedEloChange: Float): Float {
        return (resultBasedEloChange * RESULT_WEIGHT + scoreBasedEloChange * GOAL_WEIGHT)
    }
}
