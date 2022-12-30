package com.epifoos.domain.calculation.data

import com.epifoos.domain.Elo
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.calculation.data.win.WinConditionMapper
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

abstract class MatchDataMapper<W : WinConditionMapper>(val winConditionMapper: W) {
    abstract fun getMatchData(
        league: League,
        match: Match,
        players: Set<Player>,
        initialEloMap: Map<Player, Elo>
    ): MatchData
}
