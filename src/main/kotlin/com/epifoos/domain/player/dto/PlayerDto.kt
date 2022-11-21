package com.epifoos.domain.player.dto

import com.epifoos.domain.match.dto.MatchDto
import java.time.LocalDateTime

data class PlayerDto(
    val id: Int,
    val createdDate: LocalDateTime,
    val username: String,
    val elo: Float,
    val played: Int,
    val wins: Int,
    val losses: Int,
    val eloChange: Float,
    val highestElo: Float,
    val lowestElo: Float,
    val scoreFor: Int,
    val scoreAgainst: Int,
    val currentWinningStreak: Int,
    val currentLosingStreak: Int,
    val longestWinningStreak: Int,
    val longestLosingStreak: Int,
)

data class PlayerMinifiedDto (
    val id: Int,
    val username: String,
    val elo: Float
)

data class PlayerSpotlightDto(
    val player: PlayerDto,
    val match: MatchDto?
)

