package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.calculation.data.dto.GameResult
import com.epifoos.domain.player.Player

//TODO: Refactor so we don't have to implement so many functions
class HeadToHeadWinConditionMapper : WinConditionMapper() {
    override fun getWinners(players: Collection<Player>, gameResults: List<GameResult>): Set<Player> {
        val playerByWins = players.groupBy({ gameResults.count { game -> game.isWinner(it) } }, { it })

        if (playerByWins.size == 1) {
            return setOf()
        }

        return playerByWins[playerByWins.keys.max()]!!.toSet()
    }

    override fun getLosers(players: Collection<Player>, gameResults: List<GameResult>): Set<Player> {
        val playerByLosses = players.groupBy({ gameResults.count { game -> game.isLoser(it) } }, { it })

        if (playerByLosses.size == 1) {
            return setOf()
        }

        return playerByLosses[playerByLosses.keys.max()]!!.toSet()
    }
}
