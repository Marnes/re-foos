package com.epifoos.elo

import com.epifoos.match.calculation.CalculationResult
import org.jetbrains.exposed.sql.transactions.transaction

object EloService {

    fun updateElo(calculationResult: CalculationResult) {
        transaction {

        }
    }
}
