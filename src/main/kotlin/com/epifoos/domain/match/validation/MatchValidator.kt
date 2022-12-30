package com.epifoos.domain.match.validation

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.MatchSubmissionDto
import io.konform.validation.Validation
import io.konform.validation.ValidationResult

abstract class MatchValidator(league: League) {
    protected val leagueConfig = league.config

    fun validate(matchSubmissionDto: MatchSubmissionDto): ValidationResult<MatchSubmissionDto> {
        return build().validate(matchSubmissionDto)
    }

    protected abstract fun build(): Validation<MatchSubmissionDto>;
}


