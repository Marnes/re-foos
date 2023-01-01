package com.epifoos.domain.match

import com.epifoos.domain.AuditedEntity
import com.epifoos.domain.AuditedTable
import com.epifoos.domain.Elo
import com.epifoos.domain.league.*
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerWrapper
import com.epifoos.domain.stats.PlayerStats
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and

object MatchTable : AuditedTable("match") {
    var league = reference("league_id", LeagueTable)
    var season = reference("league_season_id", LeagueSeasonTable)
}

class Match(id: EntityID<Int>) : AuditedEntity(id, MatchTable) {
    companion object : IntEntityClass<Match>(MatchTable) {
        fun getLatestMatch(leagueId: Int, seasonId: Int): Match? {
            return Match.find { MatchTable.league eq leagueId and (MatchTable.season eq seasonId) }
                .orderBy(MatchTable.createdDate to SortOrder.DESC)
                .firstOrNull()
        }
    }

    var league by League referencedOn MatchTable.league
    var season by LeagueSeason referencedOn MatchTable.season
    val games by Game referrersOn GameTable.match
}

data class MatchCalculationSubmission(
    val leagueContext: LeagueContext,
    val match: Match,
    val playerWrappers: List<PlayerWrapper>,
    val initialEloMap: Map<Player, Elo>
) {
    fun getPlayers(): Set<Player> {
        return playerWrappers.map { it.player }.toSet()
    }
    fun getStats(): Set<PlayerStats> {
        return playerWrappers.mapNotNull { it.stats }.toSet()
    }

    fun getStatsMap(): Map<Player, PlayerStats> {
        return playerWrappers.filter { it.stats != null }.associateBy({ it.player }, { it.stats!! })
    }
}
