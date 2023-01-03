package com.epifoos.domain.player

import com.epifoos.domain.auth.AuthUtil
import com.epifoos.domain.league.LeagueContextHelper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.playersRoutes() {
    route("/players") {
        get {
            call.respond(
                HttpStatusCode.OK,
                PlayerService.getPlayers(LeagueContextHelper.getActive(call.parameters["leagueId"]!!.toInt()))
            )
        }

        authenticate {
            get("/current") {
                call.respond(
                    HttpStatusCode.OK,
                    PlayerService.getPlayer(
                        LeagueContextHelper.getContext(call),
                        AuthUtil.authenticatedUser(call),
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
                    LeagueContextHelper.getContext(call),
                    call.parameters["playerId"]!!.toInt()
                )
            )
        }
    }
}
