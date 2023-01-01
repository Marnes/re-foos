package com.epifoos.domain.match.dto

import com.epifoos.domain.player.dto.PlayerDto
import com.epifoos.domain.stats.dto.MatchGameStatsDto
import com.epifoos.domain.stats.dto.MatchPlayerStatsDto
import com.epifoos.domain.user.UserDto
import java.time.LocalDateTime

data class MatchDto(
    val id: Int,
    val createdDate: LocalDateTime,
    val createdBy: UserDto,
    val games: List<GameDto>,
    val players: Map<Int, PlayerDto>,
    val playerStats: Map<Int, MatchPlayerStatsDto>,
    val winners: List<Int>,
    val losers: List<Int>
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
)
