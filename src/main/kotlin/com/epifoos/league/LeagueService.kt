package com.epifoos.league

import com.epifoos.league.dto.LeagueDto
import com.epifoos.user.User
import org.jetbrains.exposed.sql.transactions.transaction

object LeagueService {

    fun createLeague(leagueDto: LeagueDto, currentUser: User): LeagueDto {
        val league = transaction {
            League.new {
                name = leagueDto.name
                createdBy = currentUser
            }
        }

        return LeagueDto(league.id.value, league.name)
    }
}
