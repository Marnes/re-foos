package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.*
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Table

abstract class BaseMatchStatsTable(name: String) : BaseIntIdTable(name) {
    var totalScored = integer("total_scored")
}

abstract class BaseMatchStats(id: EntityID<Int>, table: BaseMatchStatsTable) : BaseIntEntity(id, table) {
    var totalScored by table.totalScored
}

object MatchStatsTable : BaseMatchStatsTable("match_stats") {
    var match = reference("match_id", MatchTable)
        .uniqueIndex("match_results_unique_idx")
}

class MatchStats(id: EntityID<Int>) : BaseMatchStats(id, MatchStatsTable) {
    companion object : IntEntityClass<MatchStats>(MatchStatsTable)

    var match by Match referencedOn MatchStatsTable.match
    var winners by Player via MatchWinnersTable
    var losers by Player via MatchLosersTable
}

object GameStatsTable : BaseMatchStatsTable("game_stats") {
    var game = reference("game_id", GameTable)
        .uniqueIndex("game_results_unique_idx")
    var winner = optReference("winner", TeamTable)
    var loser = optReference("loser", TeamTable)
}

class GameStats(id: EntityID<Int>) : BaseMatchStats(id, GameStatsTable) {
    companion object : IntEntityClass<GameStats>(GameStatsTable)

    var game by Game referencedOn GameStatsTable.game
    var winner by Team optionalReferencedOn GameStatsTable.winner
    var loser by Team optionalReferencedOn GameStatsTable.loser
}

object MatchWinnersTable : Table("match_winners") {
    val matchStats = reference("match_stats_id", MatchStatsTable)
    val player = reference("player_id", PlayerTable)
    override val primaryKey = PrimaryKey(matchStats, player)
}

object MatchLosersTable : Table("match_losers") {
    val matchStats = reference("match_stats_id", MatchStatsTable)
    val player = reference("player_id", PlayerTable)
    override val primaryKey = PrimaryKey(matchStats, player)
}
