package com.epifoos.domain.highlight.factory

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.highlight.HighlightMessage
import com.epifoos.domain.highlight.HighlightMessageType
import com.epifoos.domain.league.League
import com.epifoos.domain.player.Player

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

    override fun getOther(calculationResult: CalculationResult): Set<Player> {
        if (calculationResult.matchData.losers.isEmpty()) {
            return calculationResult.matchData.players
        }

        return calculationResult.matchData.losers
    }

    override fun isDraw(calculationResult: CalculationResult): Boolean {
        return calculationResult.matchData.winners.isEmpty()
    }

    override fun supportsWhiteWash(): Boolean {
        return false //Can add later
    }
}
