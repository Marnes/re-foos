package com.epifoos.domain.user

import com.epifoos.domain.auth.AuthUtil
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    authenticate {
        route("/users/current") {
            get {
                call.respond(HttpStatusCode.OK, UserDtoMapper.map(AuthUtil.getCurrentUser(call)))
            }
        }
    }
}
