package com.epifoos.domain.highlight.factory

import com.epifoos.domain.highlight.HighlightMessage
import com.epifoos.domain.highlight.HighlightMessageType

class RoundRobinHighlightFactory(messageMap: Map<HighlightMessageType, List<HighlightMessage>>) :
    HighlightFactory(messageMap) {

}
