package com.epifoos.domain.player.dto

import com.epifoos.domain.match.dto.MatchDto
import java.time.LocalDateTime

abstract class BasePlayerDto {
    abstract val id: Int
    abstract val createdDate: LocalDateTime
    abstract val userId: Int
    abstract val username: String
    abstract val avatar: String?
    abstract val elo: Float
}

data class PlayerDto(
    override val id: Int,
    override val createdDate: LocalDateTime,
    override val userId: Int,
    override val username: String,
    override val avatar: String?,
    override val elo: Float,
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
) : BasePlayerDto()

data class PlayerMinifiedDto (
    override val id: Int,
    override val createdDate: LocalDateTime,
    override val userId: Int,
    override val username: String,
    override val avatar: String?,
    override val elo: Float
) : BasePlayerDto()

data class PlayerSpotlightDto(
    val player: PlayerDto,
    val match: MatchDto?
)

