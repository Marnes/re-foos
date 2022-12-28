package com.epifoos.domain.league.builder

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.league.dto.LeagueCreationDto
import com.epifoos.domain.user.User

object LeagueBuilderService {

    fun create(leagueCreationDto: LeagueCreationDto, currentUser: User): League {
        return getBuilder(leagueCreationDto.config.type).create(leagueCreationDto, currentUser)
    }

    private fun getBuilder(leagueType: LeagueType): LeagueBuilder {
        return when (leagueType) {
            LeagueType.HEAD_TO_HEAD -> HeadToHeadLeagueBuilder()
            LeagueType.ROUND_ROBIN -> RoundRobinLeagueBuilder()
        }
    }
}
