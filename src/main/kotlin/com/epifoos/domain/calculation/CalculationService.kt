package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.calculator.EloCalculator
import com.epifoos.domain.calculation.calculator.types.HeadToHeadCalculator
import com.epifoos.domain.calculation.calculator.types.RoundRobinCalculator
import com.epifoos.domain.calculation.coefficient.types.TeamBasedCoefficientCalculator
import com.epifoos.domain.calculation.data.win.HeadToHeadWinConditionMapper
import com.epifoos.domain.calculation.data.win.RoundRobinWinConditionMapper
import com.epifoos.domain.calculation.data.win.TeamBasedMatchDataMapper
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchUtil
import com.epifoos.domain.player.PlayerUtil

object CalculationService {

    fun calculate(league: League, match: Match): CalculationResult {
        val players = MatchUtil.getPlayers(match)
        val initialEloMap = PlayerUtil.getEloMap(players)

        return getCalculator(league).calculate(league, match, players, initialEloMap)
    }

    private fun getCalculator(league: League): EloCalculator<*, *> {
        if (league.config.type == LeagueType.ROUND_ROBIN) {
            return RoundRobinCalculator(
                TeamBasedMatchDataMapper(RoundRobinWinConditionMapper()),
                TeamBasedCoefficientCalculator()
            )
        }

        return HeadToHeadCalculator(
            TeamBasedMatchDataMapper(HeadToHeadWinConditionMapper()),
            TeamBasedCoefficientCalculator()
        )
    }
}
