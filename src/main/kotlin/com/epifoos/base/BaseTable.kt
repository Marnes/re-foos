package com.epifoos.base

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

abstract class BaseIntIdTable(name: String) : IntIdTable(name) {
    val createdDate = datetime("created_date").defaultExpression(CurrentDateTime())
}

abstract class BaseIntEntity(id: EntityID<Int>, table: BaseIntIdTable) : IntEntity(id) {
    val createdDate by table.createdDate
}

abstract class BaseTable<ID : Comparable<ID>> constructor(name: String) : IdTable<ID>(name) {
    var createdDate = datetime("created_date").defaultExpression(CurrentDateTime())
}

abstract class BaseEntity<ID : Comparable<ID>>(id: EntityID<ID>, table: BaseTable<ID>) : Entity<ID>(id) {
    val createdDate by table.createdDate
}
