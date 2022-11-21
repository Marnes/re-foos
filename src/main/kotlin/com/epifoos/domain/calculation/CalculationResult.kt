package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.coefficient.MatchCoefficients
import com.epifoos.domain.calculation.data.dto.GameData
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

data class CalculationResult(
    val match: Match,
    val matchData: MatchData,
    val coefficients: MatchCoefficients,
    val eloChanges: Map<Player, Float>,
    val gameCalculationResults: List<GameCalculationResult>
)

data class GameCalculationResult(
    val game: Game,
    val gameData: GameData,
    val eloChanges: Map<Player, Float>,
)
