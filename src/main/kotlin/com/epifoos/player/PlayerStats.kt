package com.epifoos.player

import com.epifoos.base.BaseEntity
import com.epifoos.base.BaseTable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column

object PlayerStats : BaseTable<String>("player_stats") {
    override val id: Column<EntityID<String>> = varchar("username", 255)
        .entityId()
        .uniqueIndex("player_stats_username_uniq_idx")

    var elo = float("elo")
    var played = integer("played")
    var wins = integer("wins")
    var losses = integer("losses")
    var goalsFor = integer("goals_for")
    var goalsAgainst = integer("goals_against")
    var eloChange = float("elo_change")
}

class PlayerStat(id: EntityID<String>) : BaseEntity<String>(id, PlayerStats) {
    companion object : EntityClass<String, PlayerStat>(PlayerStats) {
        private const val DEFAULT_ELO = 1000F

        fun createDefaults(username: String): PlayerStat {
            return PlayerStat.new(username) {
                elo = DEFAULT_ELO
                played = 0
                wins = 0
                losses = 0
                goalsFor = 0
                goalsAgainst = 0
                eloChange = 0F
            }
        }
    }

    val player by Player backReferencedOn Players.id
    var elo by PlayerStats.elo
    var played by PlayerStats.played
    var wins by PlayerStats.wins
    var losses by PlayerStats.losses
    var goalsFor by PlayerStats.goalsFor
    var goalsAgainst by PlayerStats.goalsAgainst
    var eloChange by PlayerStats.eloChange
}
