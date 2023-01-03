package com.epifoos.domain.league.dto

import com.epifoos.domain.Elo
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.match.dto.MatchDto
import com.epifoos.domain.player.dto.PlayerMinifiedDto
import com.epifoos.domain.user.UserDto
import java.time.LocalDate
import java.time.LocalDateTime

data class LeagueDto(
    val id: Int,
    val name: String,
    val joined: Boolean,
    val isOpen: Boolean,
    val createdDate: LocalDateTime,
    val createdBy: UserDto,
    val season: Int,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val players: Long,
    val matches: Long,
    val leader: PlayerMinifiedDto?,
    val latestMatch: MatchDto?,
    val config: LeagueConfigDto,
)

data class LeagueConfigDto(
    val startingElo: Elo,
    val type: LeagueType,
    val games: Int,
    val teams: Int,
    val players: Int,
    val scoresPerTeam: Int,
    val playersPerTeam: Int,
    val maxScore: Int
)

data class LeagueCreationDto(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val coefficients: LeagueCoefficientsDto,
    val config: LeagueConfigDto
)

data class LeagueCoefficientsDto(
    val kValue: Double,
    val resultCoefficient: Int
)
