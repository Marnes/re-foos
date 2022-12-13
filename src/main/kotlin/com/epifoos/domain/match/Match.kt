package com.epifoos.domain.match

import com.epifoos.domain.AuditedEntity
import com.epifoos.domain.AuditedTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder

object MatchTable : AuditedTable("match") {
    var league = reference("league_id", LeagueTable)
}

class Match(id: EntityID<Int>) : AuditedEntity(id, MatchTable) {
    companion object : IntEntityClass<Match>(MatchTable) {
        fun getLatestMatch(leagueId: Int): Match? {
            return Match.find { MatchTable.league eq leagueId }
                .orderBy(MatchTable.createdDate to SortOrder.DESC)
                .firstOrNull()
        }
    }

    var league by League referencedOn MatchTable.league
    val games by Game referrersOn GameTable.match
}
