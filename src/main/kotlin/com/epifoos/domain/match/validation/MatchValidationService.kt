package com.epifoos.domain.match.validation

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.MatchSubmissionDto
import com.epifoos.domain.user.User
import io.konform.validation.ValidationResult

object MatchValidationService {

    fun validate(
        league: League,
        matchSubmissionDto: MatchSubmissionDto,
        currentUser: User
    ): ValidationResult<MatchSubmissionDto> {
        return getValidator(league).validate(matchSubmissionDto, currentUser)
    }

    private fun getValidator(league: League): MatchValidator {
        return RoundRobinMatchValidator(league)
    }
}
