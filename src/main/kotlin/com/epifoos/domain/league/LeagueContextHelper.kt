package com.epifoos.domain.league

import com.epifoos.exceptions.EntityNotFoundException
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.transactions.transaction

object LeagueContextHelper {
    fun getContext(call: ApplicationCall): LeagueContext {
        return transaction {
            val leagueId = call.parameters["leagueId"]!!.toInt()
            val season = call.request.queryParameters["season"]?.toInt()

            val league =
                League.findById(leagueId) ?: throw EntityNotFoundException("Could not find league with id $leagueId")
            val leagueSeason = LeagueSeason.find(leagueId, season)

            LeagueContext(league, leagueSeason)
        }
    }

    fun getActive(leagueId: Int): LeagueContext {
        return transaction {
            val league =
                League.findById(leagueId) ?: throw EntityNotFoundException("Could not find league with id $leagueId")
            val leagueSeason = LeagueSeason.findActive(leagueId)
                ?: throw EntityNotFoundException("No active seasons for league with id $leagueId")

            LeagueContext(league, leagueSeason)
        }
    }
}


data class LeagueContext(
    val league: League,
    val season: LeagueSeason
) {
    fun leagueId(): Int {
        return league.id.value
    }

    fun seasonId(): Int {
        return season.id.value
    }
}


