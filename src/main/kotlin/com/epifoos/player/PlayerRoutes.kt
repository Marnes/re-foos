package com.epifoos.player

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.playersRoutes() {
    route("/players") {
        get {
            call.respond(HttpStatusCode.OK, PlayerService.getPlayers())
        }
    }
}
