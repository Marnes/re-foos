package com.epifoos.domain.match

import com.epifoos.domain.Elo
import com.epifoos.domain.league.League
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerWrapper

object MatchSubmissionHelper {

    fun getPlayers(match: Match): Set<Player> {
        return match.games.flatMap { it.teams }.flatMap { it.players }.toSet()
    }

    fun getInitialEloMap(league: League, playerWrappers: List<PlayerWrapper>): Map<Player, Elo> {
        return playerWrappers.associateBy({ it.player }, { it.stats?.elo ?: league.config.startingElo })
    }

}
