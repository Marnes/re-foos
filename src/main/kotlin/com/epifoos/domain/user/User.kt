package com.epifoos.domain.user

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.Players
import com.epifoos.ilike
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Users : BaseIntIdTable("user") {
    var username = varchar("username", 255)
        .uniqueIndex("player_username_uniq_idx")
    var password = varchar("password", 255)
    var avatar = varchar("avatar", 255)
}

class User(id: EntityID<Int>) : BaseIntEntity(id, Users) {
    companion object : IntEntityClass<User>(Users) {
        fun findByUsername(username: String): User? {
            return User.find { Users.username ilike username }.firstOrNull()
        }
    }

    var username by Users.username
    var password by Users.password
    var avatar by Users.avatar

    val player by Player backReferencedOn Players.user
}
