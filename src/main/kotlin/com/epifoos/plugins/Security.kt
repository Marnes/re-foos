package com.epifoos.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.epifoos.config.Config
import com.epifoos.exceptions.AuthenticationException
import com.epifoos.security.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {

    val jwtConfig = Config.getJwtConfig();

    val verifier: JWTVerifier = JWT
        .require(jwtConfig.algorithm)
        .withIssuer(jwtConfig.issuer)
        .build()


    install(Authentication) {
        jwt {
            realm = jwtConfig.realm

            verifier(verifier)

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
