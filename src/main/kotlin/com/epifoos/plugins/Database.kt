package com.epifoos.plugins

import com.epifoos.config.Config
import com.epifoos.domain.league.LeagueConfigTable
import com.epifoos.domain.league.LeagueTable
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.stats.*
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun Application.configureDatabase() {
    val databaseConfig = Config.getDatabaseConfig()

    Flyway.configure().dataSource(databaseConfig.url, databaseConfig.user, databaseConfig.password).load().migrate()

    Database.connect(
        databaseConfig.url,
        driver = databaseConfig.driver,
        user = databaseConfig.user,
        password = databaseConfig.password
    )

    writeSchema()
}

fun writeSchema() {
    transaction {
        val schema = SchemaUtils.createStatements(
            GamePlayerStatsTable,
            GameResultStatsTable,
            MatchPlayerStatsTable,
            MatchResultStatsTable,
            PlayerStatsTable,
            LeagueStatsTable,
            RandomStatsTable,
            PlayerTable,
            LeagueTable,
            LeagueConfigTable,
        ).joinToString("\n\n") { "$it;" }
        File("schema.sql").writeText(schema)
    }
}
