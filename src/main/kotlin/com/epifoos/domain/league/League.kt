package com.epifoos.domain.league

import com.epifoos.domain.AuditedEntity
import com.epifoos.domain.AuditedTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Leagues : AuditedTable("league") {
    var name = varchar("name", 255)
}

class League(id: EntityID<Int>) : AuditedEntity(id, Leagues) {
    companion object : IntEntityClass<League>(Leagues)

    var name by Leagues.name
}

