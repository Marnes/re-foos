package com.epifoos.domain.league.builder

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig
import com.epifoos.domain.league.LeagueHelper
import com.epifoos.domain.league.dto.LeagueConfigDto
import com.epifoos.domain.league.dto.LeagueCreationDto
import com.epifoos.domain.user.User

abstract class LeagueBuilder {

    open fun create(leagueCreationDto: LeagueCreationDto, currentUser: User): League {
        return League.new {
            name = leagueCreationDto.name
            uid = LeagueHelper.generateUid()
            createdBy = currentUser
        }.also { createConfig(it, leagueCreationDto.config) }
    }

    abstract fun createConfig(league: League, leagueConfigDto: LeagueConfigDto): LeagueConfig

}
