package com.epifoos.domain.calculation.data

import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.calculation.data.win.WinConditionMapper
import com.epifoos.domain.match.Match

abstract class MatchDataMapper<W: WinConditionMapper>(val winConditionMapper: W) {
    abstract fun getMatchData(match: Match): MatchData
}
