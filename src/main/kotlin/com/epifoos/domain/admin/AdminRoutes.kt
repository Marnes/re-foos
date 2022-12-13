package com.epifoos.domain.admin

import com.epifoos.domain.league.League
import com.epifoos.domain.user.profile.AvatarService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.adminRoutes() {
    route("/admin") {
        authenticate("basic") {
            post("/recalculate") {
                AdminService.recalculate(
                    transaction {
                        League.findById(call.request.queryParameters["leagueId"]!!.toInt())!!
                    }
                )
                call.respond(HttpStatusCode.OK)
            }

            post("/avatars") {
                AvatarService.populateAvatars()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
