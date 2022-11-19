package com.epifoos.match.calculation

import com.epifoos.domain.match.Game
import com.epifoos.domain.player.Player

data class CalculationResult(
    val players: List<Player>,
    val eloChanges: Map<Player, Float>,
    val gameCalculationResults: List<GameCalculationResult>
)

data class GameCalculationResult(
    val game: Game,
    val eloChanges: Map<Player, Float>
)
