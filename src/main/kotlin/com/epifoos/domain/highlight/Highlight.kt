package com.epifoos.domain.highlight

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.GameTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object HighlightTable : BaseIntIdTable("highlight") {
    var match = reference("match_id", MatchTable)
    var game = optReference("game_id", GameTable)
    var message = reference("highlight_message_id", HighlightMessageTable)
}

class Highlight(id: EntityID<Int>) : BaseIntEntity(id, HighlightTable) {
    companion object : IntEntityClass<Highlight>(HighlightTable)

    var match by Match referencedOn HighlightTable.match
    var game by Game optionalReferencedOn HighlightTable.game
    var message by HighlightMessage referencedOn HighlightTable.message

    val players by HighlightPlayer referrersOn HighlightPlayerTable.highlight
}

object HighlightPlayerTable : BaseIntIdTable("highlight_player") {
    var highlight = reference("highlight_id", HighlightTable)
    var player = reference("player_id", PlayerTable)
    var result = enumerationByName<HighlightPlayerResult>("result", 100)
}

class HighlightPlayer(id: EntityID<Int>) : BaseIntEntity(id, HighlightPlayerTable) {
    companion object : IntEntityClass<HighlightPlayer>(HighlightPlayerTable)

    var highlight by Highlight referencedOn HighlightPlayerTable.highlight
    var player by Player referencedOn HighlightPlayerTable.player
    var result by HighlightPlayerTable.result
}

enum class HighlightPlayerResult {
    WIN,
    LOSE,
    OTHER
}
