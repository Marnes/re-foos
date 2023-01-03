package com.epifoos.domain.admin

import com.epifoos.domain.highlight.HighlightService
import com.epifoos.domain.league.LeagueContext
import com.epifoos.domain.match.*
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerLeagueRepository
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.rank.PlayerRankService
import com.epifoos.domain.stats.StatsService
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction

object AdminService {

    fun recalculate(leagueContext: LeagueContext) {
        transaction {
            val players = Player.find { PlayerTable.league eq leagueContext.league.id }
                .with(Player::user)
                .toList()

            val matches = Match.find { MatchTable.season eq leagueContext.seasonId() }
                .orderBy(MatchTable.createdDate to SortOrder.ASC)
                .toList()

            val games = Game.find { GameTable.season eq leagueContext.seasonId() }.toList()

            val playerIds = players.map { it.id.value }
            val matchIds = matches.map { it.id.value }
            val gameIds = games.map { it.id.value }

            StatsService.resetStats(playerIds, matchIds, gameIds, leagueContext.season)
            HighlightService.resetHighlights(matchIds)
            PlayerRankService.resetRanks(playerIds, matchIds, leagueContext.season)

            matches.forEach {
                val matchPlayers = MatchSubmissionHelper.getPlayers(it)
                val playerWrappers =
                    PlayerLeagueRepository.findByIds(matchPlayers.map { player -> player.id.value }, leagueContext)
                val initialEloMap = MatchSubmissionHelper.getInitialEloMap(leagueContext.league, playerWrappers)

                MatchEngine.calculate(MatchCalculationSubmission(leagueContext, it, playerWrappers, initialEloMap))
            }
        }
    }
}
