package com.epifoos.domain.stats.dto

data class MatchPlayerStatsDto(
    val eloChange: Float,
    val scoreFor: Int,
    val scoreAgainst: Int,
    val winner: Boolean,
    val loser: Boolean
)

data class MatchGameStatsDto(
    val eloChange: Float,
    val scoreFor: Int,
    val scoreAgainst: Int,
)
