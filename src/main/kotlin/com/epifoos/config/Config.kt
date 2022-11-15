package com.epifoos.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object Config {
    private val config = HoconApplicationConfig(ConfigFactory.load())

    fun getProperty(key: String): String? = config.propertyOrNull(key)?.getString()

    fun getDatabaseConfig(): DatabaseConfig = DatabaseConfig(
        url = getProperty("database.url")!!,
        driver = getProperty("database.driver")!!,
        user = getProperty("database.user")!!,
        password = getProperty("database.password")!!,
    )

    fun getJwtConfig(): JwtConfig = JwtConfig(
        realm = getProperty("jwt.realm")!!,
        secret = getProperty("jwt.secret")!!,
        issuer = getProperty("jwt.issuer")!!,
        audience = getProperty("jwt.audience")!!
    )

    fun getPort(): Int = getProperty("ktor.deployment.port")!!.toInt()
}
