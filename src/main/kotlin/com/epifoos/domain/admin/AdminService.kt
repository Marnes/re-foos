package com.epifoos.domain.admin

import com.epifoos.domain.highlight.HighlightService
import com.epifoos.domain.league.League
import com.epifoos.domain.match.*
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.rank.PlayerRankService
import com.epifoos.domain.stats.StatsService
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction

object AdminService {

    fun recalculate(league: League) {
        transaction {
            val players = Player.find { PlayerTable.league eq league.id }
                .with(Player::stats, Player::rank, Player::user)
                .toList()

            val matches = Match.find { MatchTable.league eq league.id }
                .orderBy(MatchTable.createdDate to SortOrder.ASC)
                .toList()

            val games = Game.find { GameTable.match inList matches.map { it.id.value } }.toList()

            StatsService.resetStats(league, players, matches, games)
            HighlightService.resetHighlights(players, matches)
            PlayerRankService.resetRanks(players, matches)

            matches.forEach { MatchEngine.calculate(league, it) }
        }
    }
}
