package com.epifoos.domain.match.dto

data class MatchSubmissionDto(
    val games: List<GameSubmissionDto>
) {

    fun getPlayers(): Set<Int> {
        return games.flatMap { it.teams }.flatMap { it.players }.toSet()
    }
}

data class GameSubmissionDto(
    val teams: List<TeamSubmissionDto>
)

data class TeamSubmissionDto(
    val players: Set<Int>,
    val scores: List<Int>
)
