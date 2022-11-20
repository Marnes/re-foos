package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.coefficient.MatchCoefficients
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
) {
    fun getEloChange(player: Player): Float {
        return eloChanges[player]!!
    }
}

data class GameCalculationResult(
    val game: Game,
    val eloChanges: Map<Player, Float>,
)
