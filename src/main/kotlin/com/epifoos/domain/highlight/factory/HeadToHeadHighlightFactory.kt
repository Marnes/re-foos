package com.epifoos.domain.highlight.factory

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.highlight.HighlightMessage
import com.epifoos.domain.highlight.HighlightMessageType
import com.epifoos.domain.player.Player

class HeadToHeadHighlightFactory(messageMap: Map<HighlightMessageType, List<HighlightMessage>>) :
    HighlightFactory(messageMap) {

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

    override fun getLosers(calculationResult: CalculationResult): Set<Player> {
        return setOf()
    }
}
