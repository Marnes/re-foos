package com.epifoos.player

import com.epifoos.user.User

data class PlayerDTO(
    val username: String,
    val elo: Float,
    val played: Int,
    val wins: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsForAvg: Float,
    val goalsAgainst: Int,
    val goalsAgainstAvg: Float,
    val eloChange: Float,
) {
    companion object {
        fun fromPlayer(user: User): PlayerDTO {
            val player = user.player

            return PlayerDTO(
                user.username,
                player.stats.elo,
                player.stats.played,
                player.stats.wins,
                player.stats.losses,
                player.stats.goalsFor,
                if (player.stats.played == 0) 0F else player.stats.goalsFor.toFloat() / player.stats.played,
                player.stats.goalsAgainst,
                if (player.stats.played == 0) 0F else player.stats.goalsAgainst.toFloat() / player.stats.played,
                player.stats.eloChange
            )
        }
    }
}
