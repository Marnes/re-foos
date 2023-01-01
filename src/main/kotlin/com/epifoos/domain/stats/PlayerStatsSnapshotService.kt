package com.epifoos.domain.stats

import com.epifoos.domain.match.Match
import org.jetbrains.exposed.dao.with

object PlayerStatsSnapshotService {

    fun createSnapshots(match: Match, playerStats: Set<PlayerStats>) {
        playerStats.forEach { createSnapshot(match, it) }
    }

    fun getStatsSnapshot(match: Match): List<MatchPlayerStatsSnapshot> {
        return MatchPlayerStatsSnapshot.find { MatchPlayerStatsSnapshotTable.match eq match.id }
            .with(MatchPlayerStatsSnapshot::player)
            .toList()
    }

    private fun createSnapshot(match: Match, playerStats: PlayerStats) {
        MatchPlayerStatsSnapshot.new {
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
