package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.id.EntityID

object RandomStatsTable : BaseIntIdTable("random_stats") {
    var league = reference("league_id", LeagueTable)
    var player = reference("player_id", PlayerTable)
    var amount = float("amount")
    var type = enumerationByName("type", 255, RandomStatType::class)
}

class RandomStats(id: EntityID<Int>) : BaseIntEntity(id, RandomStatsTable) {
    var league by League referencedOn LeagueStatsTable.league
    var player by Player referencedOn PlayerStatsTable.player
    var amount by RandomStatsTable.amount
    var type by RandomStatsTable.type
}

enum class RandomStatType {
    WINNING_STREAK,
    LOSING_STREAK
}

