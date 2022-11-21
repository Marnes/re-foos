package com.epifoos.domain.league.dto

import com.epifoos.domain.league.League

object LeagueDtoMapper {
    fun map(league: League): LeagueDto {
        return LeagueDto(league.id.value, league.name)
    }
}
