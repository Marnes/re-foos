package com.epifoos.player

import com.epifoos.base.BaseIntEntity
import com.epifoos.base.BaseIntIdTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object PlayerStats : BaseIntIdTable("player_stats") {
    var player = reference("player_id", Players)
        .uniqueIndex("player_stats_player_uniq_idx")

    var elo = float("elo")
    var played = integer("played")
    var wins = integer("wins")
    var losses = integer("losses")
    var goalsFor = integer("goals_for")
    var goalsAgainst = integer("goals_against")
    var eloChange = float("elo_change")
}

class PlayerStat(id: EntityID<Int>) : BaseIntEntity(id, PlayerStats) {
    companion object : IntEntityClass<PlayerStat>(PlayerStats) {
        private const val DEFAULT_ELO = 1000F

        fun createDefaults(player: Player): PlayerStat {
            return PlayerStat.new {
                this.player = player
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

    var player by Player referencedOn PlayerStats.player
    var elo by PlayerStats.elo
    var played by PlayerStats.played
    var wins by PlayerStats.wins
    var losses by PlayerStats.losses
    var goalsFor by PlayerStats.goalsFor
    var goalsAgainst by PlayerStats.goalsAgainst
    var eloChange by PlayerStats.eloChange
}
