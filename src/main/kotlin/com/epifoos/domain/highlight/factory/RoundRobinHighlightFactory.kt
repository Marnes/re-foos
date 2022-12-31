package com.epifoos.domain.highlight.factory

import com.epifoos.domain.highlight.HighlightMessage
import com.epifoos.domain.highlight.HighlightMessageType
import com.epifoos.domain.league.League

class RoundRobinHighlightFactory(league: League, messageMap: Map<HighlightMessageType, List<HighlightMessage>>) :
    HighlightFactory(league, messageMap) {

}
