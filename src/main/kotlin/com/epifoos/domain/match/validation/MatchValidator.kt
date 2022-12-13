package com.epifoos.domain.match.validation

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.GameSubmissionDto
import com.epifoos.domain.match.dto.MatchSubmissionDto
import com.epifoos.domain.match.dto.TeamSubmissionDto
import com.epifoos.domain.user.User
import com.epifoos.plugins.rangeInclusive
import com.epifoos.plugins.size
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.onEach

open class MatchValidator(league: League) {

    companion object {
        private const val GAME_COUNT = 3
        private const val PLAYER_COUNT = 4
        private const val TEAM_COUNT = 2
        private const val TEAM_SCORE_COUNT = 2
        private const val TEAM_PLAYER_COUNT = 2
        private const val MAX_SCORE = 5

        private const val MAX_GAME_SCORE = TEAM_SCORE_COUNT * MAX_SCORE
    }

    fun validate(matchSubmissionDto: MatchSubmissionDto, currentUser: User): ValidationResult<MatchSubmissionDto> {
        return build().validate(matchSubmissionDto)
    }

    private fun build(): Validation<MatchSubmissionDto> {
        return Validation {
            MatchSubmissionDto::players required {
                size(PLAYER_COUNT)
            }

            MatchSubmissionDto::games required {
                size(GAME_COUNT)
                run(validateTeam())

                onEach {
                    run(validateWinners())

                    GameSubmissionDto::teams required {
                        size(TEAM_COUNT)

                        onEach {
                            TeamSubmissionDto::scores required {
                                size(TEAM_SCORE_COUNT)
                                run(validateTotalScore())

                                onEach {
                                    rangeInclusive(0..MAX_SCORE)
                                }
                            }

                            TeamSubmissionDto::players required {
                                size(TEAM_PLAYER_COUNT)
                            }
                        }
                    }
                }
            }
        }
    }

    protected open fun validateWinners(): Validation<GameSubmissionDto> {
        return Validation {
            addConstraint("Each game should have a winner") {
                hasWinnerForAll(it)
            }
        }
    }

    protected open fun validateTeam(): Validation<List<GameSubmissionDto>> {
        return Validation {
            addConstraint("Players must match for all games") { allTeamsMatch(it) }
        }
    }

    protected open fun validateTotalScore(): Validation<List<Int>> {
        return Validation {
            addConstraint("Total score cannot be more than {0}", MAX_GAME_SCORE.toString()) {
                it.sum() <= MAX_GAME_SCORE
            }
        }
    }

    private fun allTeamsMatch(games: List<GameSubmissionDto>): Boolean {
        return true  //TODO: Implement. Each game must have the same teams
    }

    private fun hasWinnerForAll(game: GameSubmissionDto): Boolean {
        val teamWins = game.teams.associateBy({ it }, { getWinCount(it) })

        return teamWins.values.sum() == TEAM_SCORE_COUNT
    }

    private fun getWinCount(team: TeamSubmissionDto): Int {
        return team.scores.filter { it == MAX_SCORE }.size
    }
}


