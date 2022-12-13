package com.epifoos.domain.stats.dto

import com.epifoos.domain.Elo

data class MatchPlayerStatsDto(
    val eloChange: Elo,
    val scoreFor: Int,
    val scoreAgainst: Int,
)

data class MatchGameStatsDto(
    val eloChange: Elo,
    val scoreFor: Int,
    val scoreAgainst: Int,
)
