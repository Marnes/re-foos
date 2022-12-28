package com.epifoos.domain.league.dto

import com.epifoos.domain.Elo
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.user.UserDto
import java.time.LocalDate

data class LeagueDto(
    val id: Int,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val isClosed: Boolean,
    val joined: Boolean,
    val config: LeagueConfigDto,
)

data class LeagueCreationDto(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val config: LeagueConfigDto
)

data class LeagueSummaryDto(
    val id: Int,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val joined: Boolean,
    val teamComposition: String,
    val players: Int,
    val createdBy: UserDto,
    val type: LeagueType,
    val isClosed: Boolean
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
