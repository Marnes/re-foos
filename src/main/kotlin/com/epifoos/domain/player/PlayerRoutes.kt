package com.epifoos.domain.player

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.playersRoutes() {
    route("/players") {
        get {
            call.respond(HttpStatusCode.OK, PlayerService.getPlayers(call.parameters["leagueId"]!!.toInt()))
        }

        get("/spotlight") {
            call.respond(HttpStatusCode.OK, PlayerService.getPlayerSpotlight(call.parameters["leagueId"]!!.toInt()))
        }
    }
}
