package com.epifoos.domain.highlight

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.highlight.dto.HighlightDto
import com.epifoos.domain.highlight.dto.HighlightDtoMapper
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object HighlightService {

    fun resetHighlights(league: League) {
        transaction {
            HighlightTable.deleteAll()
            HighlightPlayerTable.deleteAll()
        }
    }

    fun createHighlights(calculationResult: CalculationResult) {
        transaction {
            HighlightFactory(getMessagesMap()).createHighlights(calculationResult)
        }
    }

    fun getLatestHighlights(leagueId: Int): List<HighlightDto> {
        return transaction { Match.getLatestMatch(leagueId)?.let { getLatestHighlights(it) } } ?: listOf()
    }

    private fun getLatestHighlights(match: Match): List<HighlightDto> {
        return Highlight.find { HighlightTable.match eq match.id }
            .with(Highlight::match)
            .with(Highlight::players)
            .with(HighlightPlayer::player)
            .with(Player::user)
            .map { HighlightDtoMapper.map(it) }
    }

    private fun getMessagesMap(): Map<HighlightMessageType, List<HighlightMessage>> {
        return HighlightMessage.all().groupBy { it.type }
    }
}
