package com.epifoos.domain.match

import com.epifoos.domain.AuditedEntity
import com.epifoos.domain.AuditedTable
import com.epifoos.game.GameOld
import com.epifoos.game.GamesOld
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Matches : AuditedTable(("match")) {
    var league = reference("league_id", com.epifoos.domain.league.Leagues)
}

class Match(id: EntityID<Int>) : AuditedEntity(id, Matches) {
    companion object : IntEntityClass<Match>(Matches)

    var league by com.epifoos.domain.league.League referencedOn Matches.league
    val games by Game referrersOn Games.match
    val gamesOld by GameOld referrersOn GamesOld.match

}
