package com.epifoos.domain.stats.dto

import com.epifoos.domain.stats.GamePlayerStats
import com.epifoos.domain.stats.MatchPlayerStats
import com.epifoos.domain.stats.MatchStats

object MatchPlayerStatsDtoMapper {

    fun mapMatchStats(stats: List<MatchPlayerStats>, matchStats: MatchStats): Map<Int, MatchPlayerStatsDto> {
        return stats.associateBy({ it.player.id.value }, {
            MatchPlayerStatsDto(
                it.eloChange,
                it.scoreFor,
                it.scoreAgainst,
                matchStats.winner == it.player,
                matchStats.loser == it.player
            )
        })
    }

    fun mapGameStats(stats: List<GamePlayerStats>): Map<Int, MatchGameStatsDto> {
        return stats.associateBy({ it.player.id.value }, {
            MatchGameStatsDto(
                it.eloChange,
                it.scoreFor,
                it.scoreAgainst,
            )
        })
    }
}

