package com.epifoos.domain.league.dto

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig
import com.epifoos.domain.user.UserDtoMapper

object LeagueDtoMapper {

    fun map(league: League, joined: Boolean): LeagueDto {
        return LeagueDto(
            league.id.value,
            league.name,
            league.startDate,
            league.endDate,
            league.isClosed(),
            joined,
            mapConfig(league.config),
        )
    }

    fun mapSummary(league: League, joined: Boolean): LeagueSummaryDto {
        return LeagueSummaryDto(
            league.id.value,
            league.name,
            league.startDate,
            league.endDate,
            joined,
            getTeamComposition(league.config),
            0,
            UserDtoMapper.map(league.createdBy),
            league.config.type,
            league.isClosed(),
        )
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
            leagueConfig.maxScore
        )
    }

    private fun getTeamComposition(leagueConfig: LeagueConfig): String {
        return MutableList(leagueConfig.teams) { leagueConfig.playersPerTeam.toString() }
            .joinToString(" v ")
    }
}
