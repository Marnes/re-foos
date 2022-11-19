package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.Matches
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.Players
import com.epifoos.game.GameOld
import com.epifoos.game.GamesOld
import org.jetbrains.exposed.dao.id.EntityID

abstract class BaseResultStats(name: String) : BaseIntIdTable(name) {
    var winner = reference("winner", Players)
    var loser = reference("loser", Players)
    var eloChange = float("eloChange")
}

abstract class BaseResultStat(id: EntityID<Int>, table: BaseResultStats) : BaseIntEntity(id, table) {
    var winner by Player referencedOn table.winner
    var loser by Player referencedOn table.loser
    var eloChange by table.eloChange
}

object MatchResultStats : BaseResultStats("match_stats") {
    var match = reference("match_id", Matches)
        .uniqueIndex("match_results_unique_idx")
}

class MatchResultStat(id: EntityID<Int>) : BaseResultStat(id, MatchResultStats) {
    var match by Match referencedOn MatchResultStats.match
}

object GameResultStats : BaseResultStats("game_stats") {
    var game = reference("game_id", GamesOld)
        .uniqueIndex("game_results_unique_idx")
}

class GameResultStat(id: EntityID<Int>) : BaseResultStat(id, GameResultStats) {
    var gameOld by GameOld referencedOn GameResultStats.game
}
