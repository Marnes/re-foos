package com.epifoos.domain.calculation.coefficient

import com.epifoos.domain.calculation.coefficient.dto.MatchCoefficients
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.match.Match

abstract class CoefficientCalculator {

    abstract fun calculateCoefficient(
        match: Match,
        matchData: MatchData,
    ): MatchCoefficients
}

