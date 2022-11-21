package com.epifoos.domain.match

import com.epifoos.domain.AuditedEntity
import com.epifoos.domain.AuditedTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueTable
import com.epifoos.game.GameOld
import com.epifoos.game.GamesOld
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.with

object MatchTable : AuditedTable("match") {
    var league = reference("league_id", LeagueTable)
}

class Match(id: EntityID<Int>) : AuditedEntity(id, MatchTable) {
    companion object : IntEntityClass<Match>(MatchTable) {
        fun getCompleteMatch(matchId: Int): Match? {
            return Match.find { MatchTable.id eq matchId }
                .with(Match::games)
                .with(Game::teams)
                .with(Team::players)
//                .with(Team::scores)
//                .with(TeamPlayer::player)
                .firstOrNull()
        }
    }

    var league by League referencedOn MatchTable.league
    val games by Game referrersOn GameTable.match
    val gamesOld by GameOld referrersOn GamesOld.match

}
