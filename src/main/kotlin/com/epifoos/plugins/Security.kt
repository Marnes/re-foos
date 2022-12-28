package com.epifoos.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.epifoos.config.Config
import com.epifoos.domain.auth.AuthService
import com.epifoos.domain.user.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureSecurity() {

    val jwtConfig = Config.getJwtConfig();

    val verifier: JWTVerifier = JWT
        .require(jwtConfig.algorithm)
        .withIssuer(jwtConfig.issuer)
        .build()

    install(Authentication) {
        basic("basic") {
            validate { credentials ->
                AuthService.authenticate(credentials.name, credentials.password)
            }
        }

        jwt {
            realm = jwtConfig.realm
            verifier(verifier)
            validate { credential ->
                if (credential.payload.audience.contains(Config.getJwtConfig().audience)) {
                    transaction { User.findById(credential.payload.claims["id"]!!.asInt())!! }
                } else {
                    null
                }
            }
        }
    }
}


