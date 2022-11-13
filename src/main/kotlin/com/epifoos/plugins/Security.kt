package com.epifoos.plugins

import com.epifoos.config.Config
import com.epifoos.security.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {

    install(Authentication) {
        jwt {
            realm = "EPI-Foos"

            verifier(
                JwtService.verifier
            )

            challenge { _, _ ->
                throw AuthenticationException()
            }

            validate { credential ->
                if (credential.payload.audience.contains(Config.getJwtConfig().audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
