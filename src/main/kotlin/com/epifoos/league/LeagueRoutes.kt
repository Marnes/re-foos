package com.epifoos.league

import com.epifoos.auth.AuthUtil
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
}
