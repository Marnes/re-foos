package com.epifoos.domain.calculation

enum class MatchResult(val weight: Double) {
    WIN(4.0),
    WIN_ON_SCORE(3.0),
    DRAW(2.0),
    LOSE_ON_SCORE(1.0),
    LOSE(0.0);

    companion object {
        fun max(): MatchResult = WIN
    }
}
