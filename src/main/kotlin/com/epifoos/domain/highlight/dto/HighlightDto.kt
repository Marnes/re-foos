package com.epifoos.domain.highlight.dto

import com.epifoos.domain.highlight.HighlightMessageType
import com.epifoos.domain.highlight.HighlightPlayerResult
import com.epifoos.domain.player.dto.PlayerMinifiedDto
import java.time.LocalDateTime

data class HighlightDto(
    val createdDate: LocalDateTime,
    val matchId: Int,
    val gameId: Int?,
    val message: String,
    val messageType: HighlightMessageType,
    val players: List<HighlightPlayerDto>
)

data class HighlightPlayerDto(
    val player: PlayerMinifiedDto,
    val result: HighlightPlayerResult
)
