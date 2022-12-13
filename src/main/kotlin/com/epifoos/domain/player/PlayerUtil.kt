package com.epifoos.domain.player

import com.epifoos.domain.Elo

object PlayerUtil {

    fun getEloMap(players: Collection<Player>): Map<Player, Elo> {
        return players.associateWith { it.stats.elo }
    }
}
