package com.epifoos.domain.match.validation

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.MatchSubmissionDto
import io.konform.validation.Validation

class HeadToHeadMatchValidator(league: League): MatchValidator(league)  {

    override fun build(): Validation<MatchSubmissionDto> {
        return Validation {}
    }
}
