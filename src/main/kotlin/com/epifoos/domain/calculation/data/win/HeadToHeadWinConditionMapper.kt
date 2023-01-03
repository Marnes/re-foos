package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.calculation.data.dto.GameResult
import com.epifoos.domain.player.Player

//TODO: Refactor so we don't have to implement so many functions
class HeadToHeadWinConditionMapper : WinConditionMapper() {
    override fun getWinners(
        players: Collection<Player>,
        gameResults: List<GameResult>,
        scoreForMap: Map<Player, Int>
    ): Set<Player> {
        val playerByWins = players.groupBy({ gameResults.count { game -> game.isWinner(it) } }, { it })

        if (playerByWins.keys.size > 1) { //Only 1 winner as there are 2 different scores
            return playerByWins[playerByWins.keys.max()]!!.toSet()
        }

        val playerByScores = scoreForMap.entries.groupBy({ it.value }, { it.key })

        if (playerByScores.keys.size > 1) {
            return playerByScores[playerByScores.keys.max()]!!.toSet()
        }

        return setOf() //Win Count and Score count is the same, thus a draw
    }

    override fun getLosers(
        players: Collection<Player>,
        gameResults: List<GameResult>,
        scoreForMap: Map<Player, Int>
    ): Set<Player> {
        val playerByLosses = players.groupBy({ gameResults.count { game -> game.isLoser(it) } }, { it })

        if (playerByLosses.keys.size > 1) {
            return playerByLosses[playerByLosses.keys.max()]!!.toSet()
        }

        val playerByScores = scoreForMap.entries.groupBy({ it.value }, { it.key })

        if (playerByScores.keys.size > 1) {
            return playerByScores[playerByScores.keys.min()]!!.toSet()
        }

        return setOf() //Win Count and Score count is the same, thus a draw
    }
}
