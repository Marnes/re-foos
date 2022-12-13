package com.epifoos.domain.calculation.coefficient.types

import com.epifoos.domain.Elo
import com.epifoos.domain.calculation.MatchResult
import com.epifoos.domain.calculation.coefficient.CoefficientCalculator
import com.epifoos.domain.calculation.coefficient.GameCoefficients
import com.epifoos.domain.calculation.coefficient.MatchCoefficients
import com.epifoos.domain.calculation.data.dto.GameData
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player
import kotlin.math.pow

class RoundRobinCoefficientCalculator : CoefficientCalculator() {
    companion object {
        private const val ELO_WEIGHT = 400.0F
        private const val MAX_SCORE = 10
    }

    override fun calculateCoefficient(
        match: Match,
        matchData: MatchData,
    ): MatchCoefficients {
        return MatchCoefficients(match.games.associateWith {
            calculateCoefficient(
                matchData.players,
                matchData.gameDataMap[it]!!,
            )
        })
    }

    private fun calculateCoefficient(
        players: Set<Player>,
        gameData: GameData,
    ): GameCoefficients {
        return GameCoefficients(
            getExpectedCoefficientMap(players, gameData),
            getActualCoefficientMap(players, gameData),
            getResultCoefficientMap(players, gameData)
        )
    }

    private fun getExpectedCoefficientMap(players: Set<Player>, gameData: GameData): Map<Player, Double> {
        return players.associateWith { getExpectedCoefficient(it, gameData) }
    }

    private fun getActualCoefficientMap(players: Set<Player>, gameData: GameData): Map<Player, Double> {
        return players.associateWith { getActualCoefficient(it, gameData) }
    }

    private fun getResultCoefficientMap(players: Set<Player>, gameData: GameData): Map<Player, Double> {
        return players.associateWith { getResultCoefficient(it, gameData) }
    }

    private fun getExpectedCoefficient(player: Player, gameData: GameData): Double {
        return estimateScoreVersus(gameData.getTeamAverageElo(player), gameData.getOpponentTeamAverageElo(player))
    }

    private fun getActualCoefficient(player: Player, gameData: GameData): Double {
        return normalizeScore(gameData.getScoreFor(player), gameData.getOpponentAverageScore(player))
    }

    private fun getResultCoefficient(player: Player, gameData: GameData): Double {
        return gameData.getResult(player).weight / MatchResult.max().weight
    }

    private fun estimateScoreVersus(teamAverageElo: Elo, opponentAverageElo: Elo): Double {
        return 1 / (1 + 10.0.pow((opponentAverageElo.toDouble() - teamAverageElo.toDouble()) / ELO_WEIGHT))
    }

    private fun normalizeScore(teamTotalScore: Int, opponentAverageScore: Double): Double {
        return ((teamTotalScore - opponentAverageScore) + MAX_SCORE) / (2.0F * MAX_SCORE)
    }

}

