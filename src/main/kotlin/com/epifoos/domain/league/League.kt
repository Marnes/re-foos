package com.epifoos.domain.league

import com.epifoos.domain.AuditedEntity
import com.epifoos.domain.AuditedTable
import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object LeagueTable : AuditedTable("league") {
    var name = varchar("name", 255)
}

class League(id: EntityID<Int>) : AuditedEntity(id, LeagueTable) {
    companion object : IntEntityClass<League>(LeagueTable)

    var name by LeagueTable.name
    val config by LeagueConfig backReferencedOn LeagueConfigTable.league
}


object LeagueConfigTable : BaseIntIdTable("league_config") {
    var league = reference("league_id", LeagueTable)
        .uniqueIndex("league_config_unique_idx")

    var startingElo = float("starting_elo")
}

class LeagueConfig(id: EntityID<Int>) : BaseIntEntity(id, LeagueConfigTable) {
    companion object : IntEntityClass<LeagueConfig>(LeagueConfigTable)

    var league by League referencedOn LeagueConfigTable.league
    var startingElo by LeagueConfigTable.startingElo
}


