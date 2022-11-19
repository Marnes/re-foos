package com.epifoos.domain.match.dto

data class MatchSubmissionDto(
    var players: List<Int>,
    var games: List<GameSubmissionDto>
)

data class GameSubmissionDto(
    var leftPlayer1: Int,
    var leftPlayer2: Int,
    var rightPlayer1: Int,
    var rightPlayer2: Int,
    var leftScore1: Int,
    var leftScore2: Int,
    var rightScore1: Int,
    var rightScore2: Int
)
