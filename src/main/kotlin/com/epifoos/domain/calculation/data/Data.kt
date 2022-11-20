package com.epifoos.domain.calculation.data.dto

import com.epifoos.domain.calculation.MatchResult
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Team
import com.epifoos.domain.player.Player

data class MatchData(
    val players: Set<Player>,
    val winners: Set<Player>,
    val losers: Set<Player>,
    val scoreForMap: Map<Player, Int>,
    val scoreAgainstMap: Map<Player, Int>,
    val gameWinsMap: Map<Player, Int>,
    val gameLossesMap: Map<Player, Int>,
    val roundWinsMap: Map<Player, Int>,
    val roundLossesMap: Map<Player, Int>,
    val gameDataMap: Map<Game, GameData>
) {
    fun getGameData(game: Game): GameData {
        return gameDataMap[game]!!
    }

    fun isWinner(player: Player): Boolean {
        return winners.contains(player)
    }

    fun isLoser(player: Player): Boolean {
        return losers.contains(player)
    }

    fun getScoreFor(player: Player): Int {
        return scoreForMap[player]!!
    }

    fun getScoreAgainst(player: Player): Int {
        return scoreAgainstMap[player]!!
    }
}

class GameData(
    val playerTeamMap: Map<Player, Team>,
    val initialEloMap: Map<Player, Float>,
    val winner: Team?,
    val loser: Team?,
    val totalPlayed: Int,
    val scoreForMap: Map<Team, Int>,
    val scoreAgainstMap: Map<Team, Int>,
    val winsMap: Map<Team, Int>,
    val lossesMap: Map<Team, Int>,
    val resultMap: Map<Team, MatchResult>,
    val teamAverageElo: Map<Team, Float>
) {
    fun getTeam(player: Player): Team {
        return playerTeamMap[player]!!
    }

    fun isWinner(player: Player): Boolean {
        return winner == getTeam(player)
    }

    fun isLoser(player: Player): Boolean {
        return loser == getTeam(player)
    }

    fun getTeamAverageElo(player: Player): Float {
        return teamAverageElo[getTeam(player)]!!
    }

    fun getOpponentTeamAverageElo(player: Player): Float {
        return teamAverageElo
            .filter { it.key != getTeam(player) }
            .map { it.value }
            .average()
            .toFloat()
    }

    fun getTotalScore(player: Player): Int {
        return scoreForMap[getTeam(player)]!!
    }

    fun getOpponentAverageScore(player: Player): Float {
        return scoreForMap
            .filter { it.key != getTeam(player) }
            .map { it.value }
            .average()
            .toFloat()
    }

    fun getOpponentTotalScore(player: Player): Int {
        return scoreAgainstMap[getTeam(player)]!!
    }

    fun getResult(player: Player): MatchResult {
        return resultMap[getTeam(player)]!!
    }

    fun getWins(player: Player): Int {
        return winsMap[getTeam(player)]!!
    }

    fun getLosses(player: Player): Int {
        return lossesMap[getTeam(player)]!!
    }
}
