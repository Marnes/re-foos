package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.GameCalculationResult
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player
import org.jetbrains.exposed.dao.with

object MatchPlayerStatsService {

    fun createStats(calculationResult: CalculationResult) {
        calculationResult.matchData.players.forEach { createMatchStats(calculationResult, it) }
    }

    fun getMatchPlayerStats(match: Match): List<MatchPlayerStats> {
        return MatchPlayerStats.find { MatchPlayerStatsTable.match eq match.id }
            .with(MatchPlayerStats::match, MatchPlayerStats::player)
            .toList()
    }

    fun getGamePlayerStats(games: List<Game>): Map<Game, List<GamePlayerStats>> {
        return GamePlayerStats.find { GamePlayerStatsTable.game inList games.map { it.id.value } }
            .with(GamePlayerStats::game, GamePlayerStats::player)
            .groupBy { it.game }
    }

    private fun createMatchStats(calculationResult: CalculationResult, player: Player) {
        MatchPlayerStats.new {
            this.player = player
            match = calculationResult.match
            eloChange = calculationResult.eloChanges[player]!!
            scoreFor = calculationResult.matchData.scoreForMap[player]!!
            scoreAgainst = calculationResult.matchData.scoreAgainstMap[player]!!
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
