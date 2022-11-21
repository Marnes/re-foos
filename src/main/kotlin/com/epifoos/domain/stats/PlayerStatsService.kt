package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.league.League
import com.epifoos.domain.player.Player

object PlayerStatsService {

    fun createDefault(league: League, player: Player) {
        PlayerStats.new {
            this.player = player
            elo = league.config.startingElo
            played = 0
            wins = 0
            losses = 0
            scoreFor = 0
            scoreAgainst = 0
            eloChange = 0F
            highestElo = league.config.startingElo
            lowestElo = league.config.startingElo
            winningStreak = 0
            losingStreak = 0
            longestWinningStreak = 0
            longestLosingStreak = 0
        }
    }

    fun updateStats(calculationResult: CalculationResult) {
        calculationResult.matchData.players.forEach { updatePlayerStats(it, calculationResult) }
    }

    private fun updatePlayerStats(player: Player, calculationResult: CalculationResult) {
        val eloChange = calculationResult.eloChanges[player]!!

        player.stats.elo = player.stats.elo + eloChange
        player.stats.eloChange = eloChange
        player.stats.played += 1
        player.stats.scoreFor += calculationResult.matchData.scoreForMap[player]!!
        player.stats.scoreAgainst += calculationResult.matchData.scoreAgainstMap[player]!!

        if (calculationResult.matchData.isWinner(player)) {
            player.stats.wins += 1
            player.stats.winningStreak += 1
        } else {
            player.stats.winningStreak = 0
        }

        if (calculationResult.matchData.isLoser(player)) {
            player.stats.losses += 1
            player.stats.losingStreak += 1
        } else {
            player.stats.losingStreak = 0
        }

        if (player.stats.elo > player.stats.highestElo) {
            player.stats.highestElo = player.stats.elo
        }

        if (player.stats.elo < player.stats.lowestElo) {
            player.stats.lowestElo = player.stats.elo
        }

        if (player.stats.winningStreak > player.stats.longestWinningStreak) {
            player.stats.longestWinningStreak = player.stats.winningStreak
        }

        if (player.stats.losingStreak > player.stats.longestLosingStreak) {
            player.stats.longestLosingStreak = player.stats.losingStreak
        }
    }
}
