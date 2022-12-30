package com.epifoos.domain.calculation.data.dto

import com.epifoos.domain.Elo
import com.epifoos.domain.average
import com.epifoos.domain.calculation.GameDifference
import com.epifoos.domain.calculation.MatchResult
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Team
import com.epifoos.domain.player.Player

data class MatchData(
    val players: Set<Player>,
    val initialEloMap: Map<Player, Elo>,
    val winners: Set<Player>,
    val losers: Set<Player>,
    val totalScored: Int,
    val scoreForMap: Map<Player, Int>,
    val scoreAgainstMap: Map<Player, Int>,
    val gameDataMap: Map<Game, GameData>
) {
    fun isWinner(player: Player): Boolean {
        return winners.contains(player)
    }

    fun isLoser(player: Player): Boolean {
        return losers.contains(player)
    }
}

data class GameData(
    val teams: Map<Team, List<Player>>,
    val playerTeamMap: Map<Player, Team>,
    val gameResult: GameResult,
    val totalPlayed: Int,
    val totalScored: Int,
    val scoreForMap: Map<Team, Int>,
    val scoreAgainstMap: Map<Team, Int>,
    val teamAverageElo: Map<Team, Elo>
) {
    fun getTeam(player: Player): Team {
        return playerTeamMap[player]!!
    }

    fun getTeamAverageElo(player: Player): Elo {
        return teamAverageElo[getTeam(player)]!!
    }

    fun getOpponentTeamAverageElo(player: Player): Elo {
        return teamAverageElo
            .filter { it.key != getTeam(player) }
            .map { it.value }
            .average()
    }

    fun getOpponents(team: Team): List<Player> {
        return teams.filter { it.key != team }
            .flatMap { it.value }
    }

    fun getScoreFor(player: Player): Int {
        return scoreForMap[getTeam(player)]!!
    }

    fun getScoreAgainst(player: Player): Int {
        return scoreAgainstMap[getTeam(player)]!!
    }

    fun getOpponentAverageScore(player: Player): Double {
        return scoreForMap
            .filter { it.key != getTeam(player) }
            .map { it.value }
            .average()
    }

    //TODO: Refactor
    fun getResult(player: Player): MatchResult {
        if (gameResult.isDraw) {
            return MatchResult.DRAW
        }

        return if (gameResult.isWinner(player)) {
            if (gameResult.difference!! == GameDifference.ON_SCORE) {
                MatchResult.WIN_ON_SCORE
            } else {
                MatchResult.WIN
            }
        } else {
            if (gameResult.difference!! == GameDifference.ON_SCORE) {
                MatchResult.LOSE_ON_SCORE
            } else {
                MatchResult.LOSE
            }
        }
    }
}

data class GameResult(
    val winners: Team?,
    val losers: Team?,
    val isDraw: Boolean,
    val difference: GameDifference?
) {
    fun isWinner(player: Player): Boolean {
        return winners?.players?.contains(player) ?: false
    }

    fun isLoser(player: Player): Boolean {
        return losers?.players?.contains(player) ?: false
    }
}
