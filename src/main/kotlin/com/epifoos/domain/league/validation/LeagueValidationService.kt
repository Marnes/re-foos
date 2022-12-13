package com.epifoos.domain.league.validation

import com.epifoos.domain.league.dto.LeagueDto
import io.konform.validation.ValidationResult

object LeagueValidationService {

    fun validate(leagueDto: LeagueDto): ValidationResult<LeagueDto> {
        return getValidator().validate(leagueDto)
    }

    private fun getValidator(): LeagueValidator {
        return LeagueValidator()
    }

}
