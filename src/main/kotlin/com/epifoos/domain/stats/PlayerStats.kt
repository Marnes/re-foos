package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

abstract class BasePlayerStatsTable(name: String) : BaseIntIdTable(name) {
    var player = reference("player", PlayerTable)
    var eloChange = float("elo_change")
}

abstract class BasePlayerStats(id: EntityID<Int>, table: BasePlayerStatsTable) : BaseIntEntity(id, table) {
    var player by Player referencedOn table.player
    var eloChange by table.eloChange
}

object PlayerStatsTable : BasePlayerStatsTable("player_stats") {
    var elo = float("elo")
    var played = integer("played")
    var wins = integer("wins")
    var losses = integer("losses")
    var scoreFor = integer("score_for")
    var scoreAgainst = integer("score_against")
    var winningStreak = integer("winning_streak")
    var losingStreak = integer("losing_streak")

    init {
        uniqueIndex("player_stats_unique_idx", player)
    }
}

class PlayerStats(id: EntityID<Int>) : BasePlayerStats(id, PlayerStatsTable) {
    companion object : IntEntityClass<PlayerStats>(PlayerStatsTable)

    var elo by PlayerStatsTable.elo
    var played by PlayerStatsTable.played
    var wins by PlayerStatsTable.wins
    var losses by PlayerStatsTable.losses
    var scoreFor by PlayerStatsTable.scoreFor
    var scoreAgainst by PlayerStatsTable.scoreAgainst
    var winningStreak by PlayerStatsTable.winningStreak
    var losingStreak by PlayerStatsTable.losingStreak

    fun setDefaults(league: League) {
        elo = league.config.startingElo
        played = 0
        wins = 0
        losses = 0
        scoreFor = 0
        scoreAgainst = 0
        winningStreak = 0
        losingStreak = 0
        eloChange = 0F
    }
}

