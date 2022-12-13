package com.epifoos.domain.stats.dto

import com.epifoos.domain.stats.GamePlayerStats
import com.epifoos.domain.stats.MatchPlayerStats

object MatchPlayerStatsDtoMapper {

    fun mapMatchStats(matchPlayerStats: MatchPlayerStats): MatchPlayerStatsDto {
        return MatchPlayerStatsDto(
            matchPlayerStats.eloChange,
            matchPlayerStats.scoreFor,
            matchPlayerStats.scoreAgainst,
        )
    }

    fun mapGameStats(gamePlayerStats: GamePlayerStats): MatchGameStatsDto {
        return MatchGameStatsDto(
            gamePlayerStats.eloChange,
            gamePlayerStats.scoreFor,
            gamePlayerStats.scoreAgainst,
        )
    }
}

