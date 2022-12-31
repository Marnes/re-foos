package com.epifoos.domain.highlight.factory

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.highlight.HighlightMessage
import com.epifoos.domain.highlight.HighlightMessageType
import com.epifoos.domain.league.League

class HeadToHeadHighlightFactory(league: League, messageMap: Map<HighlightMessageType, List<HighlightMessage>>) :
    HighlightFactory(league, messageMap) {

    override fun hasWinner(calculationResult: CalculationResult): Boolean {
        return calculationResult.matchData.winners.isNotEmpty()
    }

    override fun hasLoser(calculationResult: CalculationResult): Boolean {
        return false
    }

    override fun hasWinnerAndLoser(calculationResult: CalculationResult): Boolean {
        return false
    }

    override fun isDraw(calculationResult: CalculationResult): Boolean {
        return calculationResult.matchData.winners.isEmpty()
    }

}
