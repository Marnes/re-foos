package com.epifoos.domain.match

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueSeason
import com.epifoos.domain.league.LeagueSeasonTable
import com.epifoos.domain.league.LeagueTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object GameTable : BaseIntIdTable(("game")) {
    var league = reference("league_id", LeagueTable)
    var season = reference("league_season_id", LeagueSeasonTable)
    var match = reference("match_id", MatchTable)
}

class Game(id: EntityID<Int>) : BaseIntEntity(id, GameTable) {
    companion object : IntEntityClass<Game>(GameTable)

    var league by League referencedOn GameTable.league
    var season by LeagueSeason referencedOn GameTable.season
    var match by Match referencedOn GameTable.match
    val teams by Team referrersOn TeamTable.game
}
