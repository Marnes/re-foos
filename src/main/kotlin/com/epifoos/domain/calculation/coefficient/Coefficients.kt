package com.epifoos.domain.calculation.coefficient.dto

import com.epifoos.domain.match.Game
import com.epifoos.domain.player.Player

data class MatchCoefficients(
    val gameCoefficients: Map<Game, GameCoefficients>
) {
    fun getCoefficient(game: Game): GameCoefficients {
        return gameCoefficients[game]!!
    }
}

data class GameCoefficients(
    val expectedScoreCoefficientMap: Map<Player, Float>,
    val actualScoreCoefficientMap: Map<Player, Float>,
    val resultScoreCoefficientMap: Map<Player, Float>,
) {
    fun getExpectedScoreCoefficient(player: Player): Float {
        return expectedScoreCoefficientMap[player]!!
    }

    fun getActualScoreCoefficient(player: Player): Float {
        return actualScoreCoefficientMap[player]!!
    }

    fun getResultScoreCoefficient(player: Player): Float {
        return resultScoreCoefficientMap[player]!!
    }
}
