package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.Leagues
import org.jetbrains.exposed.dao.id.EntityID

object LeagueStats : BaseIntIdTable("league_stats") {
    var league = reference("league_id", Leagues)
        .uniqueIndex("league_stats_unique_idx")
}

class LeagueStat(id: EntityID<Int>) : BaseIntEntity(id, LeagueStats) {
    var league by League referencedOn LeagueStats.league
}
