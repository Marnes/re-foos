package com.epifoos.domain.match

import com.epifoos.domain.player.Player

object MatchUtil {

    fun getPlayers(match: Match): Set<Player> {
        return match.games.flatMap { it.teams }
            .flatMap { it.players }
            .toSet()
    }
}
