package com.epifoos.player.stats

import com.epifoos.match.Match
import com.epifoos.player.Player
import com.epifoos.player.PlayerElos
import com.epifoos.player.PlayerStat
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object StatsService {

    fun createDefault(player: Player): PlayerStat {
        return transaction {
             PlayerStat.createDefaults(player)
        }
    }

    fun resetStats() {
        transaction {
            PlayerElos.deleteAll()
            PlayerStat.all().forEach { it.reset() }
        }
    }

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
