package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.calculation.GameDifference
import com.epifoos.domain.calculation.data.dto.GameResult
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Game
import com.epifoos.domain.player.Player

abstract class WinConditionMapper {
    abstract fun getWinners(players: Collection<Player>, gameResults: List<GameResult>): Set<Player>
    abstract fun getLosers(players: Collection<Player>, gameResults: List<GameResult>): Set<Player>

    open fun getGameResult(league: League, game: Game): GameResult {
        val team1 = game.teams.toList()[0]
        val team2 = game.teams.toList()[1]

        val team1Scores = team1.scores.map { it.score }
        val team2Scores = team2.scores.map { it.score }

        val team1Wins = team1Scores.filter { it == league.config.maxScore }.size
        val team2Wins = team2Scores.filter { it == league.config.maxScore }.size

        if (team1Wins > team2Wins) {
            return GameResult(team1, team2, false, GameDifference.FULL)
        }

        if (team2Wins > team1Wins) {
            return GameResult(team2, team1, false, GameDifference.FULL)
        }

        val team1TotalScore = team1Scores.sum()
        val team2TotalScore = team2Scores.sum()

        if (team1TotalScore > team2TotalScore) {
            return GameResult(team1, team2, false, GameDifference.ON_SCORE)
        }

        if (team2TotalScore > team1TotalScore) {
            return GameResult(team2, team1, false, GameDifference.ON_SCORE)
        }

        return GameResult(null, null, true, null)
    }

}
