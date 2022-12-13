package com.epifoos.domain.league.dto

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig

object LeagueDtoMapper {
    fun map(league: League): LeagueDto {
        return LeagueDto(league.id.value, league.name, mapConfig(league.config))
    }

    private fun mapConfig(leagueConfig: LeagueConfig): LeagueConfigDto {
        return LeagueConfigDto(
            leagueConfig.startingElo,
            leagueConfig.type,
            leagueConfig.games,
            leagueConfig.teams,
            leagueConfig.players,
            leagueConfig.scoresPerTeam,
            leagueConfig.playersPerTeam,
        )
    }
}
