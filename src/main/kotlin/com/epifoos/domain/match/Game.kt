package com.epifoos.domain.match

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Games : BaseIntIdTable(("game")) {
    var match = reference("match_id", Matches)
}

class Game(id: EntityID<Int>) : BaseIntEntity(id, Games) {
    companion object : IntEntityClass<Game>(Games)

    var match by Match referencedOn Games.match
    val teams by Team referrersOn Teams.game
}
