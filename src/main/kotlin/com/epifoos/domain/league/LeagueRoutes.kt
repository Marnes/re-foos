package com.epifoos.domain.league

import com.epifoos.domain.auth.AuthUtil
import com.epifoos.domain.highlight.HighlightService
import com.epifoos.domain.match.matchRoutes
import com.epifoos.domain.player.playersRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.leagueRoutes() {
    route("/leagues") {
        authenticate("basic") {
            post {
                val league = LeagueService.createLeague(call.receive(), AuthUtil.getCurrentUser(call))
                call.respond(league)
            }
        }
    }

    route("/leagues/{leagueId}") {
        playersRoutes()
        matchRoutes()

        get("/highlights") {
            call.respond(HighlightService.getLatestHighlights(call.parameters["leagueId"]!!.toInt()))
        }
    }
}
