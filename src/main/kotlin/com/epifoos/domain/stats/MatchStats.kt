package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.GameTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.id.EntityID

abstract class BaseResultStatsTable(name: String) : BaseIntIdTable(name) {
    var winner = reference("winner", PlayerTable)
    var loser = reference("loser", PlayerTable)
    var eloChange = float("eloChange")
}

abstract class BaseResultStats(id: EntityID<Int>, table: BaseResultStatsTable) : BaseIntEntity(id, table) {
    var winner by Player referencedOn table.winner
    var loser by Player referencedOn table.loser
    var eloChange by table.eloChange
}

object MatchResultStatsTable : BaseResultStatsTable("match_stats") {
    var match = reference("match_id", MatchTable)
        .uniqueIndex("match_results_unique_idx")
}

class MatchResultStats(id: EntityID<Int>) : BaseResultStats(id, MatchResultStatsTable) {
    var match by Match referencedOn MatchResultStatsTable.match
}

object GameResultStatsTable : BaseResultStatsTable("game_stats") {
    var game = reference("game_id", GameTable)
        .uniqueIndex("game_results_unique_idx")
}

class GameResultStats(id: EntityID<Int>) : BaseResultStats(id, GameResultStatsTable) {
    var game by Game referencedOn GameResultStatsTable.game
}
