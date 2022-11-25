package com.epifoos.plugins

import com.epifoos.domain.admin.adminRoutes
import com.epifoos.domain.auth.authRoutes
import com.epifoos.domain.league.leagueRoutes
import com.epifoos.domain.player.profile.profileRoutes
import com.epifoos.domain.user.userRoutes
import com.epifoos.exceptions.AuthenticationException
import com.epifoos.exceptions.AuthorizationException
import com.epifoos.exceptions.EntityNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        route("/api") {
            authRoutes()
            adminRoutes()
            leagueRoutes()
            profileRoutes()
            userRoutes()
        }
        static("/assets") {
            staticRootFolder = File("assets")
            files(".")
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

