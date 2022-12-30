package com.epifoos.domain.league.builder

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.league.dto.LeagueConfigDto

class HeadToHeadLeagueBuilder : LeagueBuilder() {

    companion object {
        private const val TEAMS = 2
        private const val SCORES_PER_TEAM = 1
    }

    override fun createConfig(league: League, leagueConfigDto: LeagueConfigDto): LeagueConfig {
        return LeagueConfig.new {
            this.league = league
            startingElo = leagueConfigDto.startingElo
            type = LeagueType.ROUND_ROBIN
            games = leagueConfigDto.games
            teams = TEAMS
            players = TEAMS * leagueConfigDto.playersPerTeam
            scoresPerTeam = SCORES_PER_TEAM
            playersPerTeam = leagueConfigDto.playersPerTeam
            maxScore = leagueConfigDto.maxScore
        }
    }
}
