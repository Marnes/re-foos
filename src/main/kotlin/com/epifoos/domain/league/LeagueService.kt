package com.epifoos.domain.league

import com.epifoos.domain.league.dto.LeagueDto
import com.epifoos.domain.user.User
import org.jetbrains.exposed.sql.transactions.transaction

object LeagueService {

    fun createLeague(leagueDto: LeagueDto, currentUser: User): LeagueDto {
        transaction {
            League.new {
                name = leagueDto.name
                createdBy = currentUser
            }.also {
                LeagueConfig.new {
                    league = it
                    startingElo = leagueDto.startingElo
                }
            }
        }

        return leagueDto
    }
}
