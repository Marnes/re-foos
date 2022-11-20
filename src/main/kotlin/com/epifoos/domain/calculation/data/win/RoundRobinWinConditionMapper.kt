package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.calculation.MatchResult
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Team
import com.epifoos.domain.match.TeamScore
import com.epifoos.domain.player.Player

//TODO: Refactor so we don't have to implement so many functions
class RoundRobinWinConditionMapper : WinConditionMapper() {

    override fun getWinner(gameWinsMap: Map<Player, Int>): Set<Player> {
        val playersByWins = gameWinsMap.entries.groupBy({ it.value }, { it.key })
        val highestWins = playersByWins.keys.max()
        val highestWinsPlayers = playersByWins[highestWins]!!

        if (highestWinsPlayers.size == 1) {
            return highestWinsPlayers.toSet()
        }

        return setOf()
    }

    override fun getLoser(gameLossesMap: Map<Player, Int>): Set<Player> {
        val playerByLosses = gameLossesMap.entries.groupBy({ it.value }, { it.key })
        val highestLosses = playerByLosses.keys.max()
        val highestLossesPlayers = playerByLosses[highestLosses]!!

        if (highestLossesPlayers.size == 1) {
            return highestLossesPlayers.toSet()
        }

        return setOf()
    }

    override fun getWinResult(
        teamTotalWins: Map<Team, Int>,
        teamTotalScores: Map<Team, Int>
    ): Pair<Team?, MatchResult> {
        val teamsByWins = teamTotalWins.entries.groupBy({ it.value }, { it.key })
        val highestWins = teamsByWins.keys.max()
        val highestWinsTeams = teamsByWins[highestWins]!!

        val teamsByHighestScore = teamTotalScores.entries
            .filter { highestWinsTeams.contains(it.key) }
            .groupBy({ it.value }, { it.key })

        val highestScore = teamsByHighestScore.keys.max()
        val highestScoreTeams = teamsByHighestScore[highestScore]!!


        if (highestWinsTeams.size == 1) {
            return Pair(highestWinsTeams.first(), MatchResult.WIN)
        } else if (highestScoreTeams.size == 1) {
            return Pair(highestScoreTeams.first(), MatchResult.WIN_ON_SCORE)
        }

        return Pair(null, MatchResult.DRAW)
    }

    override fun getLoseResult(
        teamTotalWins: Map<Team, Int>,
        teamTotalScores: Map<Team, Int>
    ): Pair<Team?, MatchResult> {
        val teamsByWins = teamTotalWins.entries.groupBy({ it.value }, { it.key })
        val lowestWins = teamsByWins.keys.min()
        val lowestWinsTeams = teamsByWins[lowestWins]!!

        val teamsByLowestScore = teamTotalScores.entries
            .filter { lowestWinsTeams.contains(it.key) }
            .groupBy({ it.value }, { it.key })

        val lowestScore = teamsByLowestScore.keys.min()
        val lowestScoreTeams = teamsByLowestScore[lowestScore]!!

        if (lowestWinsTeams.size == 1) {
            return Pair(lowestWinsTeams.first(), MatchResult.LOSE)
        } else if (lowestScoreTeams.size == 1) {
            return Pair(lowestScoreTeams.first(), MatchResult.LOSE_ON_SCORE)
        }

        return Pair(null, MatchResult.DRAW)
    }

    override fun getTotalWinsMap(game: Game): Map<Team, Int> {
        return game.teams.associateWith { getNumberOfWins(game, it) }
    }

    override fun getTotalLossesMap(game: Game): Map<Team, Int> {
        return game.teams.associateWith { getNumberOfLosses(game, it) }
    }

    private fun getNumberOfWins(game: Game, team: Team): Int {
        return team.scores.filterIndexed { index, score -> isHighestScore(game, team, score, index) }
            .count()
    }

    private fun isHighestScore(game: Game, team: Team, score: TeamScore, index: Int): Boolean {
        return game.teams.filter { it != team }
            .map { it.scores.elementAt(index) }
            .map { it.score }
            .none { it > score.score }
    }

    private fun getNumberOfLosses(game: Game, team: Team): Int {
        return team.scores.filterIndexed { index, score -> isLowestScore(game, team, score, index) }
            .count()
    }

    private fun isLowestScore(game: Game, team: Team, score: TeamScore, index: Int): Boolean {
        return game.teams.filter { it != team }
            .map { it.scores.elementAt(index) }
            .map { it.score }
            .all { it < score.score }
    }
}
