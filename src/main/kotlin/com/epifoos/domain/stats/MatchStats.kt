package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.*
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.id.EntityID

abstract class BaseStatsTable(name: String) : BaseIntIdTable(name) {
    var goalsScored = float("goals_scored")
}

abstract class BaseResultStats(id: EntityID<Int>, table: BaseStatsTable) : BaseIntEntity(id, table) {
    var goalsScored by table.goalsScored
}

object MatchStatsTable : BaseStatsTable("match_stats") {
    var match = reference("match_id", MatchTable)
        .uniqueIndex("match_results_unique_idx")
    var winner = reference("winner", PlayerTable)
    var loser = reference("loser", PlayerTable)
}

class MatchStats(id: EntityID<Int>) : BaseResultStats(id, MatchStatsTable) {
    var match by Match referencedOn MatchStatsTable.match
    var winner by Player referencedOn MatchStatsTable.winner
    var loser by Player referencedOn MatchStatsTable.loser
}

object GameStatsTable : BaseStatsTable("game_stats") {
    var game = reference("game_id", GameTable)
        .uniqueIndex("game_results_unique_idx")
    var winner = reference("winner", TeamTable)
    var loser = reference("loser", TeamTable)
}

class GameStats(id: EntityID<Int>) : BaseResultStats(id, GameStatsTable) {
    var game by Game referencedOn GameStatsTable.game
    var winner by Player referencedOn GameStatsTable.winner
    var loser by Player referencedOn GameStatsTable.loser
}
