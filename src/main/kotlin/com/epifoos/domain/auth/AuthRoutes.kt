package com.epifoos.domain.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {

    route("/auth") {
        post("/login") {
            var jwt = AuthService.login(call.receive())
            call.respond(HttpStatusCode.OK, jwt)
        }

        authenticate("basic") {
            post("/register") {
                var jwt = AuthService.register(call.receive())
                call.respond(HttpStatusCode.OK, jwt)
            }
        }

        authenticate {
            post("/change-password") {
                AuthService.changePassword(call.receive(), AuthUtil.getCurrentUser(call))
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
