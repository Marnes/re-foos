package com.epifoos.domain.stats

import com.epifoos.domain.match.Match

object PlayerStatsSnapshotService {

    fun createSnapshots(match: Match, playerStats: List<PlayerStats>) {
        playerStats.forEach { createSnapshot(match, it) }
    }

    private fun createSnapshot(match: Match, playerStats: PlayerStats) {
        PlayerStatsSnapshot.new {
            this.player = playerStats.player
            this.match = match
            elo = playerStats.elo
            played = playerStats.played
            wins = playerStats.wins
            losses = playerStats.losses
            scoreFor = playerStats.scoreFor
            scoreAgainst = playerStats.scoreAgainst
            eloChange = playerStats.eloChange
            highestElo = playerStats.highestElo
            lowestElo = playerStats.lowestElo
            winningStreak = playerStats.winningStreak
            losingStreak = playerStats.losingStreak
            longestWinningStreak = playerStats.longestWinningStreak
            longestLosingStreak = playerStats.longestLosingStreak
        }
    }
}
