package com.epifoos.match

import com.epifoos.auth.AuthUtil
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.matchRoutes() {
    route("/matches") {
        authenticate {
            post {
                val newMatch = MatchService.captureMatch(call.receive(), AuthUtil.getCurrentUser(call))
                call.respond(newMatch)
            }
        }
    }
}
