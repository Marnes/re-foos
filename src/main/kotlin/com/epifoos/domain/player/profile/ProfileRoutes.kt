package com.epifoos.domain.player.profile

import com.epifoos.domain.auth.AuthUtil
import com.epifoos.domain.user.profile.AvatarService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.profileRoutes() {
    authenticate {
        route("/profile") {
            route("/avatars") {
                get {
                    call.respond(HttpStatusCode.OK, AvatarService.getAvailableAvatars(AuthUtil.authenticatedUser(call)))
                }

                put {
                    AvatarService.updateAvatar(AuthUtil.authenticatedUser(call), call.receive())
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}


