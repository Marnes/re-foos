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
import com.epifoos.domain.match.MatchCalculationSubmission

object CalculationService {

    fun calculate(matchCalculationSubmission: MatchCalculationSubmission): CalculationResult {
        val league = matchCalculationSubmission.leagueContext.league

        return getCalculator(league).calculate(
            league,
            matchCalculationSubmission.match,
            matchCalculationSubmission.getPlayers(),
            matchCalculationSubmission.initialEloMap
        )
    }

    private fun getCalculator(league: League): EloCalculator<*, *> {

        return TeamBasedCalculator(
            league, TeamBasedMatchDataMapper(getWinConditionMapper(league)), TeamBasedCoefficientCalculator()
        )
    }

    private fun getWinConditionMapper(league: League): WinConditionMapper {
        if (league.config.type == LeagueType.ROUND_ROBIN) {
            return RoundRobinWinConditionMapper()
        }

        return HeadToHeadWinConditionMapper()
    }
}
