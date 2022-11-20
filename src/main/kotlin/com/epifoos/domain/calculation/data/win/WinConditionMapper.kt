package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.calculation.MatchResult
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Team
import com.epifoos.domain.player.Player

abstract class WinConditionMapper {
    abstract fun getWinner(gameWinsMap: Map<Player, Int>): Set<Player>
    abstract fun getLoser(gameLossesMap: Map<Player, Int>): Set<Player>
    abstract fun getWinResult(teamTotalWins: Map<Team, Int>, teamTotalScores: Map<Team, Int>): Pair<Team?, MatchResult>
    abstract fun getLoseResult(teamTotalWins: Map<Team, Int>, teamTotalScores: Map<Team, Int>): Pair<Team?, MatchResult>
    abstract fun getTotalWinsMap(game: Game): Map<Team, Int>
    abstract fun getTotalLossesMap(game: Game): Map<Team, Int>
}
