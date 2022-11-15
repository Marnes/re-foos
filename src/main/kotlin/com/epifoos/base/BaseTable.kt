package com.epifoos.base

import com.epifoos.user.User
import com.epifoos.user.Users
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

abstract class BaseIntIdTable(name: String) : IntIdTable(name) {
    val createdDate = datetime("created_date").defaultExpression(CurrentDateTime())
    var updatedDate = datetime("updated_date").clientDefault { LocalDateTime.now() }
}

abstract class BaseIntEntity(id: EntityID<Int>, table: BaseIntIdTable) : IntEntity(id) {
    val createdDate by table.createdDate
    var updatedDate by table.updatedDate
}

abstract class BaseTable<ID : Comparable<ID>> constructor(name: String) : IdTable<ID>(name) {
    val createdDate = datetime("created_date").defaultExpression(CurrentDateTime())
    var updatedDate = datetime("updated_date").clientDefault { LocalDateTime.now() }
}

abstract class BaseEntity<ID : Comparable<ID>>(id: EntityID<ID>, table: BaseTable<ID>) : Entity<ID>(id) {
    val createdDate by table.createdDate
    var updatedDate by table.updatedDate
}

abstract class AuditedTable(name: String) : BaseIntIdTable(name) {
    var createdBy = reference("created_by", Users)
}

abstract class AuditedEntity(id: EntityID<Int>, table: AuditedTable) : BaseIntEntity(id, table) {
    var createdBy by User referencedOn  table.createdBy
}
