package com.epifoos.domain.league

import com.epifoos.domain.league.builder.LeagueBuilderService
import com.epifoos.domain.league.dto.LeagueDto
import com.epifoos.domain.league.dto.LeagueDtoMapper
import com.epifoos.domain.league.validation.LeagueValidationService
import com.epifoos.domain.user.User
import com.epifoos.exceptions.ValidationException
import io.konform.validation.Invalid
import org.jetbrains.exposed.sql.transactions.transaction

object LeagueService {

    fun createLeague(leagueDto: LeagueDto, currentUser: User): LeagueDto {
        val validationResult = LeagueValidationService.validate(leagueDto)

        if (validationResult is Invalid) {
            throw ValidationException(validationResult.errors)
        }

        val league = transaction { LeagueBuilderService.create(leagueDto, currentUser) }

        return LeagueDtoMapper.map(league)
    }
}
