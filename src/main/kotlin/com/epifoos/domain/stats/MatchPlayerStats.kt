package com.epifoos.domain.stats

import com.epifoos.domain.match.Game
import com.epifoos.domain.match.GameTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object MatchPlayerStatsTable : BasePlayerStatsTable("match_player_stats") {
    var match = reference("match_id", MatchTable)

    init {
        uniqueIndex("match_player_stats_unique_idx", match, player)
    }
}

class MatchPlayerStats(id: EntityID<Int>) : BasePlayerStats(id, MatchPlayerStatsTable) {
    companion object : IntEntityClass<MatchPlayerStats>(MatchPlayerStatsTable)

    var match by Match referencedOn MatchPlayerStatsTable.match
}

object GamePlayerStatsTable : BasePlayerStatsTable("game_player_stats") {
    var game = reference("game_id", GameTable)

    init {
        uniqueIndex("game_player_stats_unique_idx", game, player)
    }
}

class GamePlayerStats(id: EntityID<Int>) : BasePlayerStats(id, GamePlayerStatsTable) {
    companion object : IntEntityClass<GamePlayerStats>(GamePlayerStatsTable)

    var game by Game referencedOn GamePlayerStatsTable.game
}
