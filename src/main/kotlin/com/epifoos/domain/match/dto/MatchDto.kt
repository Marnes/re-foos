package com.epifoos.domain.match.dto

import com.epifoos.domain.league.dto.LeagueDto
import com.epifoos.domain.player.dto.PlayerMinifiedDto
import com.epifoos.domain.stats.dto.MatchGameStatsDto
import com.epifoos.domain.stats.dto.MatchPlayerStatsDto
import com.epifoos.domain.user.UserDto
import java.time.LocalDateTime

data class MatchDto(
    val id: Int,
    val createdDate: LocalDateTime,
    val createdBy: UserDto,
    val league: LeagueDto,
    val games: List<GameDto>,
    val players: Map<Int, PlayerMinifiedDto>,
    val playerStats: Map<Int, MatchPlayerStatsDto>,
    val winner: Int?,
    val loser: Int?
)

data class GameDto(
    val playerStats: Map<Int, MatchGameStatsDto>,
    val teams: List<TeamDto>,
    val winner: Int?,
    val loser: Int?
)

data class TeamDto(
    val id: Int,
    val scores: List<Int>,
    val players: List<Int>,
    val winner: Boolean,
    val loser: Boolean
)
