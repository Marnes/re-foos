package com.epifoos.domain.calculation.coefficient

import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match

abstract class CoefficientCalculator(val league: League) {

    abstract fun calculateCoefficient(
        match: Match,
        matchData: MatchData,
    ): MatchCoefficients
}

