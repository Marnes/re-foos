package com.epifoos

import com.epifoos.config.Config
import com.epifoos.game.Games
import com.epifoos.league.Leagues
import com.epifoos.match.Matches
import com.epifoos.player.PlayerElos
import com.epifoos.player.PlayerStats
import com.epifoos.player.Players
import com.epifoos.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(Netty, port = Config.getPort(), module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val databaseConfig = Config.getDatabaseConfig()

    Database.connect(
        databaseConfig.url,
        driver = databaseConfig.driver,
        user = databaseConfig.user,
        password = databaseConfig.password
    )

    transaction {
        SchemaUtils.create(
            Players,
            Leagues,
            Games,
            Matches,
            PlayerElos,
            PlayerStats,
        )
    }

    configureHTTP()
    configureAdministration()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureMonitoring()
}
