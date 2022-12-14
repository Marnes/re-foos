package com.epifoos.domain.league.validation

import com.epifoos.domain.league.dto.LeagueConfigDto
import com.epifoos.domain.league.dto.LeagueCreationDto
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum

//TODO: Unique validator per type
class LeagueValidator {

    companion object {
        private const val MAX_GAMES = 9
        private const val TEAMS = 2
        private const val MAX_PLAYERS_PER_TEAM = 5
        private const val MAX_SCORES_PER_TEAM = 7
    }

    fun validate(leagueCreationDto: LeagueCreationDto): ValidationResult<LeagueCreationDto> {
        return build().validate(leagueCreationDto)
    }

    private fun build(): Validation<LeagueCreationDto> {
        return Validation {
            LeagueCreationDto::name required {
                minLength(5)
                maxLength(100)
            }

            LeagueCreationDto::startDate required {
//                run(validateDates(LeagueDto::startDate, LeagueDto::endDate)) TODO:
            }

            LeagueCreationDto::config required {
                run(validateConfig())
            }
        }
    }


    private fun validateConfig(): Validation<LeagueConfigDto> {
        return Validation {
            LeagueConfigDto::startingElo required {
                minimum(500)
            }

            LeagueConfigDto::type required {}

            LeagueConfigDto::games required {
                minimum(1)
                maximum(MAX_GAMES)
            }

            LeagueConfigDto::teams required {
                minimum(TEAMS)
                maximum(TEAMS)
            }

            LeagueConfigDto::playersPerTeam required {
                minimum(1)
                maximum(MAX_PLAYERS_PER_TEAM)
            }

            LeagueConfigDto::scoresPerTeam required {
                minimum(1)
                maximum(MAX_SCORES_PER_TEAM)
            }
        }
    }
}
