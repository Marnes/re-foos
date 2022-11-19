package com.epifoos.domain.user

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import com.epifoos.ilike
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object UserTable : BaseIntIdTable("user") {
    var username = varchar("username", 255)
        .uniqueIndex("player_username_uniq_idx")
    var password = varchar("password", 255)
    var avatar = varchar("avatar", 255)
}

class User(id: EntityID<Int>) : BaseIntEntity(id, UserTable) {
    companion object : IntEntityClass<User>(UserTable) {
        fun findByUsername(username: String): User? {
            return User.find { UserTable.username ilike username }.firstOrNull()
        }
    }

    var username by UserTable.username
    var password by UserTable.password
    var avatar by UserTable.avatar

    val player by Player backReferencedOn PlayerTable.user
}
