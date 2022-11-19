package com.epifoos.domain.match.calculation

import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchUtil
import com.epifoos.domain.match.calculation.calculator.RoundRobinCalculator
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerUtil
import com.epifoos.domain.stats.StatsService
import com.epifoos.elo.EloService
import org.jetbrains.exposed.sql.transactions.transaction

object CalculationService {

    fun recalculate() {
        transaction {
            StatsService.resetStats()
            Match.all().limit(1).sortedBy { it.createdDate }.forEach { recalculate(it) }
        }
    }

    fun calculate(match: Match, initialEloMap: Map<Player, Float>) {
        val matchMapper = MatchMappingService.create(match, initialEloMap)
        val calculationResult = RoundRobinCalculator
            .create(matchMapper)
            .calculate()

        EloService.updateElo(calculationResult)
    }

    private fun recalculate(match: Match) {
        calculate(match, PlayerUtil.getEloMap(MatchUtil.getPlayers(match)))
    }
}
