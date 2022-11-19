package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueTable
import org.jetbrains.exposed.dao.id.EntityID

object LeagueStatsTable : BaseIntIdTable("league_stats") {
    var league = reference("league_id", LeagueTable)
        .uniqueIndex("league_stats_unique_idx")
}

class LeagueStats(id: EntityID<Int>) : BaseIntEntity(id, LeagueStatsTable) {
    var league by League referencedOn LeagueStatsTable.league
}
