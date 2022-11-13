package com.epifoos.player

import com.epifoos.match.Match

object StatsService {

    fun updateStats(player: Player, match: Match, eloChange: Float) {
        player.stats.elo = player.stats.elo + eloChange
        player.stats.eloChange = eloChange
        player.stats.played += 1

        if (match.getWinner() == player) player.stats.wins += 1
        if (match.getLoser() == player) player.stats.losses += 1

        player.stats.goalsFor += getGoalsFor(player, match)
        player.stats.goalsAgainst += getGoalsAgainst(player, match)
    }

    private fun getGoalsFor(player: Player, match: Match): Int {
        return match.games
            .fold(0) { initial, game -> initial + if (game.leftPlayers.contains(player)) game.leftTotal else game.rightTotal }
    }

    private fun getGoalsAgainst(player: Player, match: Match): Int {
        return match.games
            .fold(0) { initial, game -> initial + if (game.leftPlayers.contains(player)) game.rightTotal else game.leftTotal }
    }
}
