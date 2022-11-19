package com.epifoos.domain.match

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object GameTable : BaseIntIdTable(("game")) {
    var match = reference("match_id", MatchTable)
}

class Game(id: EntityID<Int>) : BaseIntEntity(id, GameTable) {
    companion object : IntEntityClass<Game>(GameTable)

    var match by Match referencedOn GameTable.match
    val teams by Team referrersOn TeamTable.game
}
