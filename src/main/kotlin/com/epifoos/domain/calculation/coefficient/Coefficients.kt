package com.epifoos.domain.calculation.coefficient

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
    val expectedScoreCoefficientMap: Map<Player, Double>,
    val actualScoreCoefficientMap: Map<Player, Double>,
    val resultScoreCoefficientMap: Map<Player, Double>,
) {
    fun getExpectedScoreCoefficient(player: Player): Double {
        return expectedScoreCoefficientMap[player]!!
    }

    fun getActualScoreCoefficient(player: Player): Double {
        return actualScoreCoefficientMap[player]!!
    }

    fun getResultScoreCoefficient(player: Player): Double {
        return resultScoreCoefficientMap[player]!!
    }
}
