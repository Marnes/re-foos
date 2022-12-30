package com.epifoos.domain.league.builder

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.league.dto.LeagueConfigDto

class RoundRobinLeagueBuilder : LeagueBuilder() {

    companion object {
        private const val TEAMS = 2
        private const val GAMES = 3
        private const val SCORES_PER_TEAM = 2
        private const val PLAYERS_PER_TEAM = 2
    }

    override fun createConfig(league: League, leagueConfigDto: LeagueConfigDto): LeagueConfig {
        return LeagueConfig.new {
            this.league = league
            startingElo = leagueConfigDto.startingElo
            type = LeagueType.ROUND_ROBIN
            games = GAMES
            teams = TEAMS
            players = TEAMS * PLAYERS_PER_TEAM
            scoresPerTeam = SCORES_PER_TEAM
            playersPerTeam = PLAYERS_PER_TEAM
            maxScore = leagueConfigDto.maxScore
        }
    }
}
