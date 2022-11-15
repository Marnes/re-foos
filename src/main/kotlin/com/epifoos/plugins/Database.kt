package com.epifoos.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val databaseConfig = com.epifoos.config.Config.getDatabaseConfig()

    Flyway.configure().dataSource(databaseConfig.url, databaseConfig.user, databaseConfig.password).load().migrate()

    Database.connect(
        databaseConfig.url,
        driver = databaseConfig.driver,
        user = databaseConfig.user,
        password = databaseConfig.password
    )
}
