package com.epifoos.domain.league

import com.epifoos.domain.league.dto.LeagueRequestDto
import com.epifoos.domain.user.User
import org.jetbrains.exposed.sql.transactions.transaction

object LeagueService {

    fun createLeague(leagueRequestDto: LeagueRequestDto, currentUser: User): LeagueRequestDto {
        transaction {
            League.new {
                name = leagueRequestDto.name
                createdBy = currentUser
            }.also {
                LeagueConfig.new {
                    league = it
                    startingElo = leagueRequestDto.startingElo
                }
            }
        }

        return leagueRequestDto
    }
}
