package com.epifoos.domain.league.builder

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.league.dto.LeagueDto
import com.epifoos.domain.user.User

object LeagueBuilderService {

    fun create(leagueDto: LeagueDto, currentUser: User): League {
        return getBuilder(leagueDto.config.type).create(leagueDto, currentUser)
    }

    private fun getBuilder(leagueType: LeagueType): LeagueBuilder {
        return when (leagueType) {
            LeagueType.HEAD_TO_HEAD -> HeadToHeadLeagueBuilder()
            LeagueType.ROUND_ROBIN -> RoundRobinLeagueBuilder()
        }
    }
}
