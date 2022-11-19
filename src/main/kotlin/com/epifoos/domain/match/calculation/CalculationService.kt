package com.epifoos.domain.match.calculation

import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchUtil
import com.epifoos.domain.match.calculation.calculator.RoundRobinCalculator
import com.epifoos.domain.player.PlayerUtil
import com.epifoos.domain.stats.StatsService
import com.epifoos.elo.EloService
import org.jetbrains.exposed.sql.transactions.transaction

object CalculationService {

    fun recalculate() {
        transaction {
            StatsService.resetStats()
            Match.all().sortedBy { it.createdDate }.forEach { calculate(it) }
        }
    }

    fun calculate(match: Match) {
        val players = MatchUtil.getPlayers(match)
        val initialEloMap = PlayerUtil.getEloMap(players)
        val matchMapper = MatchMappingService.create(match, initialEloMap)
        val calculationResult = RoundRobinCalculator
            .create(matchMapper)
            .calculate()

        EloService.updateElo(calculationResult)
        StatsService.updateStats(calculationResult)
    }

}
