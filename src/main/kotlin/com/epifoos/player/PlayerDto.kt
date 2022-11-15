package com.epifoos.player

data class PlayerDTO(
    val id: Int,
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
        fun fromPlayer(player: Player): PlayerDTO {

            return PlayerDTO(
                player.id.value,
                player.user.username,
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
