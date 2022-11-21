package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.GameCalculationResult
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

object MatchPlayerStatsService {

    fun createStats(calculationResult: CalculationResult) {
        calculationResult.matchData.players.forEach { createMatchStats(calculationResult, it) }
    }

    fun getMatchStats(match: Match): List<MatchPlayerStats> {
        return getMatchStats(listOf(match))[match]!!
    }

    fun getMatchStats(matches: List<Match>): Map<Match, List<MatchPlayerStats>> {
        return MatchPlayerStats.find { MatchPlayerStatsTable.match inList matches.map { it.id.value } }
            .groupBy { it.match }
    }

    fun getGameStats(games: List<Game>): Map<Game, List<GamePlayerStats>> {
        return GamePlayerStats.find { GamePlayerStatsTable.game inList games.map { it.id.value } }
            .groupBy { it.game }
    }

    private fun createMatchStats(calculationResult: CalculationResult, player: Player) {
        MatchPlayerStats.new {
            this.player = player
            match = calculationResult.match
            eloChange = calculationResult.eloChanges[player]!!
            scoreFor = calculationResult.matchData.scoreForMap[player]!!
            scoreAgainst = calculationResult.matchData.scoreAgainstMap[player]!!
            initialElo = calculationResult.matchData.initialEloMap[player]!!
        }

        calculationResult.gameCalculationResults.forEach { createGameStats(it, player) }
    }

    private fun createGameStats(gameCalculationResult: GameCalculationResult, player: Player) {
        GamePlayerStats.new {
            this.player = player
            game = gameCalculationResult.game
            eloChange = gameCalculationResult.eloChanges[player]!!
            scoreFor = gameCalculationResult.gameData.getScoreFor(player)
            scoreAgainst = gameCalculationResult.gameData.getScoreAgainst(player)
        }
    }
}
