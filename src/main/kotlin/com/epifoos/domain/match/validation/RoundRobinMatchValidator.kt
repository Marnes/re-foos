package com.epifoos.domain.match.validation

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.GameSubmissionDto
import io.konform.validation.Validation

class RoundRobinMatchValidator(league: League): MatchValidator(league) {

    override fun validateTeam(): Validation<List<GameSubmissionDto>> {
        return Validation {
            addConstraint("Cannot submit duplicate teams") { uniqueTeams(it) }
        }
    }

    private fun uniqueTeams(games: List<GameSubmissionDto>): Boolean {
        return true //TODO: Implement. Each game must have unique teams
    }
}
