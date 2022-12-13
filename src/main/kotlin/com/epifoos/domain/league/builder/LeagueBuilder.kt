package com.epifoos.domain.league.builder

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig
import com.epifoos.domain.league.dto.LeagueConfigDto
import com.epifoos.domain.league.dto.LeagueDto
import com.epifoos.domain.user.User

abstract class LeagueBuilder {

    open fun create(leagueDto: LeagueDto, currentUser: User): League {
        return League.new {
            name = leagueDto.name
            createdBy = currentUser
        }.also { createConfig(it, leagueDto.config) }
    }

    abstract fun createConfig(league: League, leagueConfigDto: LeagueConfigDto): LeagueConfig

}
