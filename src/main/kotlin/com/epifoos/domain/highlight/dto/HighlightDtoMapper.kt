package com.epifoos.domain.highlight.dto

import com.epifoos.domain.highlight.Highlight
import com.epifoos.domain.highlight.HighlightPlayer
import com.epifoos.domain.player.dto.PlayerDtoMapper

object HighlightDtoMapper {

    fun map(highlight: Highlight): HighlightDto {
        return HighlightDto(
            highlight.match.createdDate,
            highlight.match.id.value,
            highlight.game?.id?.value,
            highlight.message.message,
            highlight.message.type,
            highlight.players.map { mapPlayer(it) }
        )
    }

    private fun mapPlayer(highlightPlayer: HighlightPlayer): HighlightPlayerDto {
        return HighlightPlayerDto(
            PlayerDtoMapper.mapMinified(highlightPlayer.player, highlightPlayer.player.stats.elo),
            highlightPlayer.result
        )
    }
}
