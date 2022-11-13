package com.epifoos.config

import com.auth0.jwt.algorithms.Algorithm

data class JwtConfig (
    val secret: String,
    val issuer: String,
    val audience: String,
) {
    val validity = 1000 * 60 * 60 * 24
    val algorithm = Algorithm.HMAC512(secret)!!
}
