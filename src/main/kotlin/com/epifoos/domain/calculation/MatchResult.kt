package com.epifoos.domain.calculation

enum class MatchResult(val weight: Float) {
    WIN(4.0F),
    WIN_ON_SCORE(3.0F),
    DRAW(2.0F),
    LOSE_ON_SCORE(1.0F),
    LOSE(0.0F);

    companion object {
        fun max(): MatchResult = WIN
    }
}
