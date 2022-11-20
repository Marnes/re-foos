package com.epifoos.domain.calculation.data

import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.match.Match

abstract class DataMapper {

    abstract fun getMatchData(match: Match): MatchData

}
