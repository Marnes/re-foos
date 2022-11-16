package com.epifoos.match

import com.epifoos.elo.EloService
import com.epifoos.player.stats.StatsService
import org.jetbrains.exposed.sql.transactions.transaction

object CalculationService {

    fun recalculate() {
        transaction {
            StatsService.resetStats()
            Match.all().sortedBy { it.createdDate }.forEach { EloService.updateElo(it) }
        }
    }
}
