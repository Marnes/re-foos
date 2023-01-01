package com.epifoos.domain.league.dto

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig
import com.epifoos.domain.league.LeagueSeason
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.dto.PlayerDtoMapper
import com.epifoos.domain.user.UserDtoMapper

object LeagueDtoMapper {

    fun map(
        league: League,
        leagueSeason: LeagueSeason,
        leader: Player?,
        players: Long,
        matches: Long,
        joined: Boolean,
        isOpen: Boolean
    ): LeagueDto {
        return LeagueDto(
            league.id.value,
            league.name,
            joined,
            isOpen,
            league.createdDate,
            UserDtoMapper.map(league.createdBy),
            leagueSeason.season,
            leagueSeason.startDate,
            leagueSeason.endDate,
            players,
            matches,
            leader?.let { PlayerDtoMapper.mapMinified(it) },
            mapConfig(league.config)
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
