package com.epifoos.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {

    post("/auth/login") {
        var jwt = AuthService.login(call.receive())
        call.respond(HttpStatusCode.OK, jwt)
    }

    post("/auth/register") {
        var jwt = AuthService.register(call.receive())
        call.respond(HttpStatusCode.OK, jwt)
    }
}
