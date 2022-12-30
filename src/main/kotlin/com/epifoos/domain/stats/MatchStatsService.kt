package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.data.dto.GameData
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import org.jetbrains.exposed.sql.SizedCollection

object MatchStatsService {

    fun createStats(calculationResult: CalculationResult) {
        createMatchStats(calculationResult.match, calculationResult.matchData)
        calculationResult.match.games.forEach { createGameStats(it, calculationResult.matchData.gameDataMap[it]!!) }
    }

    fun getMatchStats(match: Match): MatchStats {
        return getMatchStats(listOf(match))[match]!!
    }

    fun getMatchStats(matches: List<Match>): Map<Match, MatchStats> {
        return MatchStats.find { MatchStatsTable.match inList matches.map { it.id.value } }
            .associateBy { it.match }
    }

    fun getGameStats(games: List<Game>): Map<Game, GameStats> {
        return GameStats.find { GameStatsTable.game inList games.map { it.id.value } }
            .associateBy { it.game }
    }

    private fun createMatchStats(match: Match, matchData: MatchData) {
        MatchStats.new {
            this.match = match
            totalScored = matchData.totalScored
            winners = SizedCollection(matchData.winners)
            losers = SizedCollection(matchData.losers)
        }
    }

    private fun createGameStats(game: Game, gameData: GameData) {
        GameStats.new {
            this.game = game
            totalScored = gameData.totalScored
            winner = gameData.gameResult.winners
            loser = gameData.gameResult.losers
        }
    }

}
