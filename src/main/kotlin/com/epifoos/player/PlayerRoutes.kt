package com.epifoos.player

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.playersRoutes() {
    get("/players") {
        call.respond(HttpStatusCode.OK, PlayerService.getPlayers())
    }
}
