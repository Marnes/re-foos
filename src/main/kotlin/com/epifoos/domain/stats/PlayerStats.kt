package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

abstract class BasePlayerStatsTable(name: String) : BaseIntIdTable(name) {
    var player = reference("player_id", PlayerTable)
    var eloChange = float("elo_change")
    var scoreFor = integer("score_for")
    var scoreAgainst = integer("score_against")
}

abstract class BasePlayerStats(id: EntityID<Int>, table: BasePlayerStatsTable) : BaseIntEntity(id, table) {
    var player by Player referencedOn table.player
    var eloChange by table.eloChange
    var scoreFor by table.scoreFor
    var scoreAgainst by table.scoreAgainst
}

abstract class AbstractPlayerStatsTable(name: String) : BasePlayerStatsTable(name) {
    var elo = float("elo")
    var played = integer("played")
    var wins = integer("wins")
    var losses = integer("losses")
    var highestElo = float("highest_elo")
    var lowestElo = float("lowest_elo")
    var winningStreak = integer("winning_streak")
    var losingStreak = integer("losing_streak")
    var longestWinningStreak = integer("longest_winning_streak")
    var longestLosingStreak = integer("longest_losing_streak")
}

abstract class AbstractPlayerStats(id: EntityID<Int>, table: AbstractPlayerStatsTable) : BasePlayerStats(id, table) {
    var elo by table.elo
    var played by table.played
    var wins by table.wins
    var losses by table.losses
    var highestElo by table.highestElo
    var lowestElo by table.lowestElo
    var winningStreak by table.winningStreak
    var losingStreak by table.losingStreak
    var longestWinningStreak by table.longestWinningStreak
    var longestLosingStreak by table.longestLosingStreak
}

object PlayerStatsTable : AbstractPlayerStatsTable("player_stats") {
    init {
        uniqueIndex("player_stats_unique_idx", player)
    }
}

class PlayerStats(id: EntityID<Int>) : AbstractPlayerStats(id, PlayerStatsTable) {
    companion object : IntEntityClass<PlayerStats>(PlayerStatsTable)
}


