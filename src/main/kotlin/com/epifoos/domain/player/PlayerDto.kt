package com.epifoos.domain.player

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
                player.stats.scoreFor,
                if (player.stats.played == 0) 0F else player.stats.scoreFor.toFloat() / player.stats.played,
                player.stats.scoreAgainst,
                if (player.stats.played == 0) 0F else player.stats.scoreAgainst.toFloat() / player.stats.played,
                player.stats.eloChange
            )
        }
    }
}
