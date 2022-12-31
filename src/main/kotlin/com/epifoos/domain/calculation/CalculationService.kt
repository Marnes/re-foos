package com.epifoos.domain.calculation

import com.epifoos.domain.calculation.calculator.EloCalculator
import com.epifoos.domain.calculation.calculator.types.TeamBasedCalculator
import com.epifoos.domain.calculation.coefficient.types.TeamBasedCoefficientCalculator
import com.epifoos.domain.calculation.data.win.HeadToHeadWinConditionMapper
import com.epifoos.domain.calculation.data.win.RoundRobinWinConditionMapper
import com.epifoos.domain.calculation.data.win.TeamBasedMatchDataMapper
import com.epifoos.domain.calculation.data.win.WinConditionMapper
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

        return TeamBasedCalculator(
            league,
            TeamBasedMatchDataMapper(getWinConditionMapper(league)),
            TeamBasedCoefficientCalculator()
        )
    }

    private fun getWinConditionMapper(league: League): WinConditionMapper {
        if (league.config.type == LeagueType.ROUND_ROBIN) {
            return RoundRobinWinConditionMapper()
        }

        return HeadToHeadWinConditionMapper()
    }
}
