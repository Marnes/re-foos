package com.epifoos.domain.match.validation

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.GameSubmissionDto
import com.epifoos.domain.match.dto.MatchSubmissionDto
import com.epifoos.domain.match.dto.TeamSubmissionDto
import com.epifoos.plugins.rangeInclusive
import com.epifoos.plugins.size
import io.konform.validation.Validation
import io.konform.validation.onEach

class RoundRobinMatchValidator(league: League): MatchValidator(league) {
    private val maxGameScore = leagueConfig.scoresPerTeam * leagueConfig.maxScore

    override fun build(): Validation<MatchSubmissionDto> {
        return Validation {
            MatchSubmissionDto::games required {
                run(validateTeam())

                onEach {
                    run(validateWinners())

                    GameSubmissionDto::teams required {
                        size(leagueConfig.teams)

                        onEach {
                            TeamSubmissionDto::scores required {
                                run(validateTotalScore())

                                onEach {
                                    rangeInclusive(0..leagueConfig.maxScore)
                                }
                            }

                            TeamSubmissionDto::players required {
                                size(leagueConfig.playersPerTeam)
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


    protected open fun validateTotalScore(): Validation<List<Int>> {
        return Validation {
            addConstraint("Total score cannot be more than {0}", maxGameScore.toString()) {
                it.sum() <= maxGameScore
            }
        }
    }

    private fun allTeamsMatch(games: List<GameSubmissionDto>): Boolean {
        return true  //TODO: Implement. Each game must have the same teams
    }

    private fun hasWinnerForAll(game: GameSubmissionDto): Boolean {
        val teamWins = game.teams.associateBy({ it }, { getWinCount(it) })

        return teamWins.values.sum() == leagueConfig.scoresPerTeam
    }

    private fun getWinCount(team: TeamSubmissionDto): Int {
        return team.scores.filter { it == leagueConfig.maxScore }.size
    }

    private fun validateTeam(): Validation<List<GameSubmissionDto>> {
        return Validation {
            addConstraint("Cannot submit duplicate teams") { uniqueTeams(it) }
        }
    }

    private fun uniqueTeams(games: List<GameSubmissionDto>): Boolean {
        return true //TODO: Implement. Each game must have unique teams
    }
}
