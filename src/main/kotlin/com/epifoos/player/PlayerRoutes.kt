package com.epifoos.player

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.playersRoutes() {
    get("/players") {
        var playersToGet = call.parameters["players"]

        if (!playersToGet.isNullOrEmpty()) {
            call.respond(HttpStatusCode.OK, PlayerService.getPlayers(playersToGet.split(",").map { it.trim() }))
        }

        call.respond(HttpStatusCode.OK, PlayerService.getPlayers())
    }
}
