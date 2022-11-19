package com.epifoos.domain.match

import com.epifoos.domain.AuditedEntity
import com.epifoos.domain.AuditedTable
import com.epifoos.domain.league.League
import com.epifoos.game.GameOld
import com.epifoos.game.GamesOld
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object MatchTable : AuditedTable(("match")) {
    var league = reference("league_id", com.epifoos.domain.league.LeagueTable)
}

class Match(id: EntityID<Int>) : AuditedEntity(id, MatchTable) {
    companion object : IntEntityClass<Match>(MatchTable)

    var league by League referencedOn MatchTable.league
    val games by Game referrersOn GameTable.match
    val gamesOld by GameOld referrersOn GamesOld.match

}
