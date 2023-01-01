package com.epifoos.domain.admin

import com.epifoos.domain.league.LeagueContextHelper
import com.epifoos.domain.user.profile.AvatarService
import com.epifoos.plugins.authorization.isAdmin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoutes() {
    route("/admin") {
        authenticate("basic") {
            isAdmin {
                post("/recalculate") {
                    AdminService.recalculate(LeagueContextHelper.getContext(call))
                    call.respond(HttpStatusCode.OK)
                }

                post("/avatars") {
                    AvatarService.populateAvatars()
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
