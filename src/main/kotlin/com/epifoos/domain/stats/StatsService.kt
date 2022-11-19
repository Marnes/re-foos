package com.epifoos.domain.stats

import com.epifoos.domain.player.Player
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object StatsService {

    fun createDefault(player: Player): BasePlayerStat {
        return transaction {
            PlayerStat.createDefaults(player)
        }
    }

    fun resetStats() {
        transaction {
            MatchPlayerStats.deleteAll()
            GamePlayerStats.deleteAll()
            MatchResultStats.deleteAll()
            GameResultStats.deleteAll()
            LeagueStats.deleteAll()
            RandomStats.deleteAll()
            PlayerStats.deleteAll()
            Player.all().forEach { createDefault(it) }
        }
    }

//    fun updateStats(player: Player, match: Match, eloChange: Float) {
//        player.stats.elo = player.stats.elo + eloChange
//        player.stats.eloChange = eloChange
//        player.stats.played += 1
//
//        if (match.getWinner() == player) player.basePlayerStats.wins += 1
//        if (match.getLoser() == player) player.basePlayerStats.losses += 1
//
//        player.basePlayerStats.goalsFor += getGoalsFor(player, match)
//        player.basePlayerStats.goalsAgainst += getGoalsAgainst(player, match)
//    }
//
//    private fun getGoalsFor(player: Player, match: Match): Int {
//        return match.gamesOld
//            .fold(0) { initial, game -> initial + if (game.leftPlayers.contains(player)) game.leftTotal else game.rightTotal }
//    }
//
//    private fun getGoalsAgainst(player: Player, match: Match): Int {
//        return match.gamesOld
//            .fold(0) { initial, game -> initial + if (game.leftPlayers.contains(player)) game.rightTotal else game.leftTotal }
//    }
}
