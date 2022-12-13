package com.epifoos.domain.highlight

import com.epifoos.domain.calculation.CalculationResult
import com.epifoos.domain.highlight.dto.HighlightDto
import com.epifoos.domain.highlight.dto.HighlightDtoMapper
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object HighlightService {

    fun createHighlights(calculationResult: CalculationResult) {
        transaction {
            HighlightFactory(getMessagesMap()).createHighlights(calculationResult)
        }
    }

    fun getLatestHighlights(leagueId: Int): List<HighlightDto> {
        return transaction { Match.getLatestMatch(leagueId)?.let { getLatestHighlights(it) } } ?: listOf()
    }

    fun resetHighlights(players: List<Player>, matches: List<Match>) {
        val playerIds = players.map { it.id }
        val matchIds = matches.map { it.id }

        transaction {
            HighlightPlayerTable.deleteWhere { player inList playerIds }
            HighlightTable.deleteWhere { match inList matchIds }
        }
    }

    private fun getLatestHighlights(match: Match): List<HighlightDto> {
        return Highlight.find { HighlightTable.match eq match.id }
            .with(Highlight::match, Highlight::players, HighlightPlayer::player, Player::user, Player::stats, Player::rank)
            .map { HighlightDtoMapper.map(it) }
    }

    private fun getMessagesMap(): Map<HighlightMessageType, List<HighlightMessage>> {
        return HighlightMessage.all().groupBy { it.type }
    }
}
