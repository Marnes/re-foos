package com.epifoos.security

import com.auth0.jwt.JWT
import com.epifoos.config.Config
import com.epifoos.domain.user.User
import java.util.*

object JwtService {

    private val jwtConfig = Config.getJwtConfig();

    fun generateJwt(user: User): String {
        return JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("id", user.id.value)
            .withClaim("admin", user.admin)
            .withExpiresAt(getExpiration())
            .sign(jwtConfig.algorithm)
    }

    private fun getExpiration() = Date(System.currentTimeMillis() + jwtConfig.validity)
}
