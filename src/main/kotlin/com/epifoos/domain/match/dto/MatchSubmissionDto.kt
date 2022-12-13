package com.epifoos.domain.match.dto

data class MatchSubmissionDto(
    val players: Set<Int>,
    val games: List<GameSubmissionDto>
)

data class GameSubmissionDto(
    val teams: List<TeamSubmissionDto>
)

data class TeamSubmissionDto(
    val players: Set<Int>,
    val scores: List<Int>
)
