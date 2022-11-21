package com.epifoos.domain.league.dto


data class LeagueDto(
    val id: Int,
    val name: String
)

data class LeagueRequestDto(
    val id: Int,
    val name: String,
    val startingElo: Float,
)
