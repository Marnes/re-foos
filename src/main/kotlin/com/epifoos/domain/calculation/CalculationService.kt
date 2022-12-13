package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.calculator.EloCalculator
import com.epifoos.domain.calculation.calculator.types.RoundRobinCalculator
import com.epifoos.domain.calculation.coefficient.types.RoundRobinCoefficientCalculator
import com.epifoos.domain.calculation.data.win.DefaultMatchDataMapper
import com.epifoos.domain.calculation.data.win.RoundRobinWinConditionMapper
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchUtil
import com.epifoos.domain.player.PlayerUtil

object CalculationService {

    fun calculate(league: League, match: Match): CalculationResult {
        val players = MatchUtil.getPlayers(match)
        val initialEloMap = PlayerUtil.getEloMap(players)

        return getCalculator(league).calculate(match, players, initialEloMap)
    }

    private fun getCalculator(league: League): EloCalculator<*, *> {
        return RoundRobinCalculator(
            DefaultMatchDataMapper(RoundRobinWinConditionMapper()),
            RoundRobinCoefficientCalculator()
        )
    }
}
