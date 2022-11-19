package com.epifoos.domain.match.calculation.mapper

import com.epifoos.domain.match.Game
import com.epifoos.domain.player.Player

//Need better word for score, as this is a calculated score (and not game score)
data class GameMapper(
    val game: Game,
    val expectedScoreMap: Map<Player, Float>,
    val actualScoreMap: Map<Player, Float>,
    val resultScoreMap: Map<Player, Float>,
) {
    fun getExpectedScore(player: Player): Float {
        return expectedScoreMap[player]!!
    }

    fun getActualScore(player: Player): Float {
        return actualScoreMap[player]!!
    }

    fun getResultScore(player: Player): Float {
        return resultScoreMap[player]!!
    }
}
