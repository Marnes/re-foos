package com.epifoos.domain.match.calculation.mapper

import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player

data class MatchMapper(
    val match: Match,
    val players: List<Player>,
    val gameMappers: List<GameMapper>
)
