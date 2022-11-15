package com.epifoos.plugins

import com.epifoos.auth.authRoutes
import com.epifoos.exceptions.AuthenticationException
import com.epifoos.exceptions.AuthorizationException
import com.epifoos.exceptions.EntityNotFoundException
import com.epifoos.match.matchRoutes
import com.epifoos.player.playersRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            matchRoutes()
            authRoutes()
            playersRoutes()
        }
        static("/") {
            resources("static")
        }
    }
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<EntityNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound)
        }
    }
}

