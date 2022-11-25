package com.epifoos.domain.user.profile

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object AvatarTable : BaseIntIdTable("avatar") {
    var category = varchar("category", 255)
    var path = varchar("path", 255)
    var available = bool("available").default(true)
}

class Avatar(id: EntityID<Int>) : BaseIntEntity(id, AvatarTable) {
    companion object : IntEntityClass<Avatar>(AvatarTable)

    var category by AvatarTable.category
    var path by AvatarTable.path
    var available by AvatarTable.available
}
