package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.Leagues
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.Players
import org.jetbrains.exposed.dao.id.EntityID

object RandomStats : BaseIntIdTable("random_stats") {
    var league = reference("league_id", Leagues)
    var player = reference("player_id", Players)
    var amount = float("amount")
    var type = enumerationByName("type", 255, RandomStatType::class)
}

class RandomStat(id: EntityID<Int>) : BaseIntEntity(id, RandomStats) {
    var league by League referencedOn LeagueStats.league
    var player by Player referencedOn PlayerStats.player
    var amount by RandomStats.amount
    var type by RandomStats.type
}

enum class RandomStatType {
    WINNING_STREAK,
    LOSING_STREAK
}

