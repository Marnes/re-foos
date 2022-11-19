package com.epifoos.domain.player

object PlayerUtil {

    fun getEloMap(players: Collection<Player>): Map<Player, Float> {
        return players.associateWith { it.stats.elo }
    }
}
