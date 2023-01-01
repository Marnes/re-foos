package com.epifoos.domain.stats

import com.epifoos.domain.Elo
import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.league.LeagueContext
import com.epifoos.domain.player.Player

object PlayerStatsService {

    fun updateStats(
        leagueContext: LeagueContext,
        players: Set<Player>,
        playerStatsMap: Map<Player, PlayerStats>,
        calculationResult: CalculationResult
    ) {
        players.forEach { updatePlayerStats(it, playerStatsMap[it] ?: createDefault(leagueContext, it), calculationResult) }
    }

    private fun createDefault(leagueContext: LeagueContext, player: Player): PlayerStats {
        return PlayerStats.new {
            this.player = player
            league = leagueContext.league
            season = leagueContext.season
            elo = league.config.startingElo
            played = 0
            wins = 0
            losses = 0
            scoreFor = 0
            scoreAgainst = 0
            eloChange = Elo.ZERO
            highestElo = league.config.startingElo
            lowestElo = league.config.startingElo
            winningStreak = 0
            losingStreak = 0
            longestWinningStreak = 0
            longestLosingStreak = 0
        }
    }

    private fun updatePlayerStats(player: Player, stats: PlayerStats, calculationResult: CalculationResult) {
        val eloChange = calculationResult.eloChanges[player]!!

        stats.elo = stats.elo + eloChange
        stats.eloChange = eloChange
        stats.played += 1
        stats.scoreFor += calculationResult.matchData.scoreForMap[player]!!
        stats.scoreAgainst += calculationResult.matchData.scoreAgainstMap[player]!!

        if (calculationResult.matchData.isWinner(player)) {
            stats.wins += 1
            stats.winningStreak += 1
        } else {
            stats.winningStreak = 0
        }

        if (calculationResult.matchData.isLoser(player)) {
            stats.losses += 1
            stats.losingStreak += 1
        } else {
            stats.losingStreak = 0
        }

        if (stats.elo > stats.highestElo) {
            stats.highestElo = stats.elo
        }

        if (stats.elo < stats.lowestElo) {
            stats.lowestElo = stats.elo
        }

        if (stats.winningStreak > stats.longestWinningStreak) {
            stats.longestWinningStreak = stats.winningStreak
        }

        if (stats.losingStreak > stats.longestLosingStreak) {
            stats.longestLosingStreak = stats.losingStreak
        }
    }
}
