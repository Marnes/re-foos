package com.epifoos.domain.league.dto

import com.epifoos.domain.Elo
import com.epifoos.domain.league.LeagueType


data class LeagueDto(
    val id: Int,
    val name: String,
    val config: LeagueConfigDto
)

data class LeagueConfigDto(
    val startingElo: Elo,
    val type: LeagueType,
    val games: Int,
    val teams: Int,
    val players: Int,
    val scoresPerTeam: Int,
    val playersPerTeam: Int
)
