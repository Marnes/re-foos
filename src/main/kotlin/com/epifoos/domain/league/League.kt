package com.epifoos.domain.league

import com.epifoos.domain.*
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.date

object LeagueTable : AuditedTable("league") {
    var name = varchar("name", 255)
    var startDate = date("start_date")
    var endDate = date("end_date").nullable()
    var uid = varchar("uid", 10)
        .uniqueIndex("league_unique_uid")
}

class League(id: EntityID<Int>) : AuditedEntity(id, LeagueTable) {
    companion object : IntEntityClass<League>(LeagueTable)

    var name by LeagueTable.name
    var startDate by LeagueTable.startDate
    var endDate by LeagueTable.endDate
    var uid by LeagueTable.uid

    val config by LeagueConfig backReferencedOn LeagueConfigTable.league

    fun isClosed(): Boolean {
        return endDate != null
    }
}

object LeagueConfigTable : BaseIntIdTable("league_config") {
    var league = reference("league_id", LeagueTable)
        .uniqueIndex("league_config_unique_idx")

    var startingElo = elo("starting_elo")
    var type = enumerationByName("type", 255, LeagueType::class)
    var games = integer("games")
    var teams = integer("teams")
    var players = integer("players")
    var scoresPerTeam = integer("scores_per_team")
    var playersPerTeam = integer("players_per_team")
}

class LeagueConfig(id: EntityID<Int>) : BaseIntEntity(id, LeagueConfigTable) {
    companion object : IntEntityClass<LeagueConfig>(LeagueConfigTable)

    var league by League referencedOn LeagueConfigTable.league
    var startingElo by LeagueConfigTable.startingElo
    var type by LeagueConfigTable.type
    var games by LeagueConfigTable.games
    var teams by LeagueConfigTable.teams
    var players by LeagueConfigTable.players
    var scoresPerTeam by LeagueConfigTable.scoresPerTeam
    var playersPerTeam by LeagueConfigTable.playersPerTeam
}

enum class LeagueType {
    ROUND_ROBIN,
    HEAD_TO_HEAD
}
