package com.epifoos.domain.player

import com.epifoos.domain.auth.AuthUtil
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.playersRoutes() {
    route("/players") {
        get {
            call.respond(HttpStatusCode.OK, PlayerService.getPlayers(call.parameters["leagueId"]!!.toInt()))
        }

        put("spotlight") {
            call.respond(HttpStatusCode.OK, PlayerService.getOrUpdateSpotlight(call.parameters["leagueId"]!!.toInt()))
        }

        authenticate {
            get("/current") {
                call.respond(
                    HttpStatusCode.OK,
                    PlayerService.getPlayer(
                        AuthUtil.authenticatedUser(call),
                        call.parameters["leagueId"]!!.toInt()
                    )!!
                )
            }
        }
    }

    route("/players/{playerId}") {
        get("/spotlight") {
            call.respond(
                HttpStatusCode.OK,
                PlayerService.getPlayerSpotlight(
                    call.parameters["leagueId"]!!.toInt(),
                    call.parameters["playerId"]!!.toInt()
                )
            )
        }
    }
}
