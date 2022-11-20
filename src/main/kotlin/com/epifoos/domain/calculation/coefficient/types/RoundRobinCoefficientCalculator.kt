package com.epifoos.domain.calculation.coefficient.types

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
        private const val MAX_SCORE = 5
    }

    override fun calculateCoefficient(
        match: Match,
        matchData: MatchData,
    ): MatchCoefficients {
        return MatchCoefficients(match.games.associateWith {
            calculateCoefficient(
                matchData.players,
                matchData.getGameData(it),
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

    private fun getExpectedCoefficientMap(players: Set<Player>, gameData: GameData): Map<Player, Float> {
        return players.associateWith { getExpectedCoefficient(it, gameData) }
    }

    private fun getActualCoefficientMap(players: Set<Player>, gameData: GameData): Map<Player, Float> {
        return players.associateWith { getActualCoefficient(it, gameData) }
    }

    private fun getResultCoefficientMap(players: Set<Player>, gameData: GameData): Map<Player, Float> {
        return players.associateWith { getResultCoefficient(it, gameData) }
    }

    private fun getExpectedCoefficient(player: Player, gameData: GameData): Float {
        return estimateScoreVersus(gameData.getTeamAverageElo(player), gameData.getOpponentTeamAverageElo(player))
    }

    private fun getActualCoefficient(player: Player, gameData: GameData): Float {
        return normalizeScore(gameData.getTotalScore(player), gameData.getOpponentAverageScore(player))
    }

    private fun getResultCoefficient(player: Player, gameData: GameData): Float {
        return gameData.getResult(player).weight / MatchResult.max().weight
    }

    private fun estimateScoreVersus(teamAverageElo: Float, opponentAverageElo: Float): Float {
        return 1 / (1 + 10F.pow((opponentAverageElo - teamAverageElo) / ELO_WEIGHT)) //TODO: Replace 10F
    }

    private fun normalizeScore(teamTotalScore: Int, opponentAverageScore: Float): Float {
        return (teamTotalScore - opponentAverageScore + MAX_SCORE) / (2.0F * MAX_SCORE) //TODO: Replace 2F
    }

}

