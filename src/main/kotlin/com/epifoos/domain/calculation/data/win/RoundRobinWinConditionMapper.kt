package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.calculation.data.dto.GameResult
import com.epifoos.domain.player.Player

class RoundRobinWinConditionMapper : WinConditionMapper() {
    override fun getWinners(
        players: Collection<Player>,
        gameResults: List<GameResult>,
        scoreForMap: Map<Player, Int>
    ): Set<Player> {
        val winners = players.filter { gameResults.all { result -> result.isDraw || result.isWinner(it) } }

        if (winners.size != 1) {
            return setOf()
        }

        return setOf(winners[0])
    }

    override fun getLosers(
        players: Collection<Player>,
        gameResults: List<GameResult>,
        scoreForMap: Map<Player, Int>
    ): Set<Player> {
        val losers = players.filter { gameResults.all { result -> result.isDraw || result.isLoser(it) } }

        if (losers.size != 1) {
            return setOf()
        }

        return setOf(losers[0])
    }

}
