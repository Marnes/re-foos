package com.epifoos.domain.highlight

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object HighlightMessageTable : BaseIntIdTable("highlight_message") {
    var type = enumerationByName<HighlightMessageType>("type", 255)
    var message = varchar("message", 1000)
}

class HighlightMessage(id: EntityID<Int>) : BaseIntEntity(id, HighlightMessageTable) {
    companion object : IntEntityClass<HighlightMessage>(HighlightMessageTable)

    var type by HighlightMessageTable.type
    var message by HighlightMessageTable.message
}

enum class HighlightMessageType {
    WIN,
    LOSE,
    WIN_LOSE,
    DRAW,
    WHITEWASH
}
