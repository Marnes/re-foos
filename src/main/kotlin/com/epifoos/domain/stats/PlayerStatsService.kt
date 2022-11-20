package com.epifoos.domain.stats

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.player.Player

object PlayerStatsService {

    fun updateStats(calculationResult: CalculationResult) {
        calculationResult.matchData.players.forEach { updatePlayerStats(it, calculationResult) }
    }

    private fun updatePlayerStats(player: Player, calculationResult: CalculationResult) {
        val eloChange = calculationResult.getEloChange(player)

        player.stats.elo = player.stats.elo + eloChange
        player.stats.eloChange = eloChange
        player.stats.played += 1

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

        player.stats.scoreFor += calculationResult.matchData.getScoreFor(player)
        player.stats.scoreAgainst += calculationResult.matchData.getScoreAgainst(player)
    }

}
