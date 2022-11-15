package com.epifoos.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.epifoos.config.Config
import com.epifoos.user.User
import java.util.*

object JwtService {

    private val jwtConfig = Config.getJwtConfig();

    val verifier: JWTVerifier = JWT
        .require(jwtConfig.algorithm)
        .withIssuer(jwtConfig.issuer)
        .build()

    fun generateJwt(user: User): String {
        return JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("id", user.id.value)
            .withExpiresAt(getExpiration())
            .sign(jwtConfig.algorithm)
    }

    private fun getExpiration() = Date(System.currentTimeMillis() + jwtConfig.validity)
}
