package com.epifoos.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object Config {
    private val config = HoconApplicationConfig(ConfigFactory.load())

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

    fun getSecurityConfig(): SecurityConfig = SecurityConfig(
        cors = config.propertyOrNull("security.cors")?.getList(),
    )

    fun getPort(): Int = getProperty("ktor.deployment.port")!!.toInt()

    fun getEnvironment(): String = getProperty("ktor.deployment.environment")!!

    private fun getProperty(key: String): String? = config.propertyOrNull(key)?.getString()
}
