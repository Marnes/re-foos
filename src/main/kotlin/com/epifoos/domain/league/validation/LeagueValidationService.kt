package com.epifoos.domain.league.validation

import com.epifoos.domain.league.dto.LeagueCreationDto
import io.konform.validation.ValidationResult

object LeagueValidationService {

    fun validate(leagueCreationDto: LeagueCreationDto): ValidationResult<LeagueCreationDto> {
        return getValidator().validate(leagueCreationDto)
    }

    private fun getValidator(): LeagueValidator {
        return LeagueValidator()
    }

}
