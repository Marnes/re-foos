package com.epifoos.domain.highlight.factory

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.calculation.GameCalculationResult
import com.epifoos.domain.highlight.*
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

abstract class HighlightFactory(private val messageMap: Map<HighlightMessageType, List<HighlightMessage>>) {

    companion object {
        const val WHITEWASH_VALUE = 0
    }

    fun createHighlights(calculationResult: CalculationResult) {
        createWinnerHighlight(calculationResult)
        createLoserHighlight(calculationResult)
        createWinnerLoserHighlight(calculationResult)
        createDrawHighlight(calculationResult)
        createWhitewashHighlight(calculationResult)
    }

    protected open fun hasWinner(calculationResult: CalculationResult): Boolean {
        return calculationResult.matchData.winners.isNotEmpty()
    }

    protected open fun hasLoser(calculationResult: CalculationResult): Boolean {
        return calculationResult.matchData.losers.isNotEmpty()
    }

    protected open fun hasWinnerAndLoser(calculationResult: CalculationResult): Boolean {
        return hasWinner(calculationResult) && hasLoser(calculationResult)
    }

    protected open fun isDraw(calculationResult: CalculationResult): Boolean {
        return calculationResult.matchData.winners.isEmpty() && calculationResult.matchData.losers.isEmpty()
    }

    protected open fun isWhitewash(gameCalculationResult: GameCalculationResult): Boolean {
        return gameCalculationResult.gameData.scoreForMap.containsValue(WHITEWASH_VALUE)
    }

    protected open fun getWinners(calculationResult: CalculationResult): Set<Player> {
        return calculationResult.matchData.winners
    }

    protected open fun getLosers(calculationResult: CalculationResult): Set<Player> {
        return calculationResult.matchData.losers
    }

    protected open fun getOther(calculationResult: CalculationResult): Set<Player> {
        return calculationResult.matchData.players - (getWinners(calculationResult) + getLosers(calculationResult))
    }

    private fun createWinnerHighlight(calculationResult: CalculationResult) {
        if (!hasWinner(calculationResult) || hasWinnerAndLoser(calculationResult)) {
            return
        }

        createHighlight(
            calculationResult.match,
            null,
            HighlightMessageType.WIN,
            getWinners(calculationResult),
            listOf(),
            getOther(calculationResult)
        )
    }

    private fun createLoserHighlight(calculationResult: CalculationResult) {
        if (!hasLoser(calculationResult) || hasWinnerAndLoser(calculationResult)) {
            return
        }

        createHighlight(
            calculationResult.match,
            null,
            HighlightMessageType.LOSE,
            listOf(),
            getLosers(calculationResult),
            getOther(calculationResult)
        )
    }

    private fun createWinnerLoserHighlight(calculationResult: CalculationResult) {
        if (!hasWinnerAndLoser(calculationResult)) {
            return
        }

        createHighlight(
            calculationResult.match,
            null,
            HighlightMessageType.WIN_LOSE,
            getWinners(calculationResult),
            getLosers(calculationResult),
            getOther(calculationResult)
        )
    }

    private fun createDrawHighlight(calculationResult: CalculationResult) {
        if (!isDraw(calculationResult)) {
            return
        }

        createHighlight(
            calculationResult.match,
            null,
            HighlightMessageType.DRAW,
            listOf(),
            listOf(),
            getOther(calculationResult)
        )
    }

    private fun createWhitewashHighlight(calculationResult: CalculationResult) {
        calculationResult.gameCalculationResults
            .forEach { createWhitewashHighlight(calculationResult, it) }
    }

    private fun createWhitewashHighlight(
        calculationResult: CalculationResult,
        gameCalculationResult: GameCalculationResult
    ) {
        if (!isWhitewash(gameCalculationResult)) {
            return
        }

        createHighlight(
            calculationResult.match,
            gameCalculationResult.game,
            HighlightMessageType.WHITEWASH,
            gameCalculationResult.gameData.gameResult.winners!!.players.toSet(),
            gameCalculationResult.gameData.gameResult.losers!!.players.toSet(),
            listOf()
        )
    }

    private fun createHighlight(
        match: Match,
        game: Game?,
        messageType: HighlightMessageType,
        winners: Collection<Player>,
        losers: Collection<Player>,
        other: Collection<Player>
    ) {
        Highlight.new {
            this.match = match
            this.game = game
            message = getRandomMessage(messageType)
        }.also {
            createHighlightPlayers(it, HighlightPlayerResult.WIN, winners)
            createHighlightPlayers(it, HighlightPlayerResult.LOSE, losers)
            createHighlightPlayers(it, HighlightPlayerResult.OTHER, other)
        }
    }

    private fun createHighlightPlayers(
        highlight: Highlight,
        result: HighlightPlayerResult,
        players: Collection<Player>
    ) {
        players.forEach {
            HighlightPlayer.new {
                this.highlight = highlight
                this.result = result
                player = it
            }
        }
    }

    private fun getRandomMessage(messageType: HighlightMessageType): HighlightMessage {
        return messageMap[messageType]!!.random()
    }
}
