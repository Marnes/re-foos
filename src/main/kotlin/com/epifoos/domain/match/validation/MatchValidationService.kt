package com.epifoos.domain.match.validation

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueType
import com.epifoos.domain.match.dto.MatchSubmissionDto
import io.konform.validation.ValidationResult

object MatchValidationService {

    fun validate(league: League, matchSubmissionDto: MatchSubmissionDto): ValidationResult<MatchSubmissionDto> {
        return getValidator(league).validate(matchSubmissionDto)
    }

    private fun getValidator(league: League): MatchValidator {
        if (league.config.type === LeagueType.ROUND_ROBIN) {
            return RoundRobinMatchValidator(league)
        }

        return HeadToHeadMatchValidator(league);
    }
}
