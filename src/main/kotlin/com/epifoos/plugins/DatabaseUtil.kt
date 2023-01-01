package com.epifoos.plugins

import com.epifoos.domain.league.LeagueSeasonTable
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.rank.PlayerRankTable
import com.epifoos.domain.stats.PlayerStatsTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseUtil {

    fun writeSchema() {
        transaction {
            val schema = getCreateSchemas(LeagueSeasonTable, PlayerStatsTable, PlayerRankTable, MatchTable).joinToString("\n\n") { "$it;" }
            File("schema.sql").writeText(schema)
        }
    }

    private fun getCreateSchemas(vararg tables: Table): List<String> {
        if (tables.isEmpty()) return emptyList()

        val alters = arrayListOf<String>()
        return tables.flatMap { table ->
            val (create, alter) = table.ddl.partition { it.startsWith("CREATE ") }
            val indicesDDL = table.indices.flatMap { SchemaUtils.createIndex(it) }
            alters += alter
            create + indicesDDL
        } + alters
    }
}
