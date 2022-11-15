package com.epifoos.user

import com.epifoos.base.BaseIntEntity
import com.epifoos.base.BaseIntIdTable
import com.epifoos.player.Player
import com.epifoos.player.Players
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Users : BaseIntIdTable("user") {
    var username = varchar("username", 255)
        .uniqueIndex("player_username_uniq_idx")
    var password = varchar("password", 255)
}

class User(id: EntityID<Int>) : BaseIntEntity(id, Users) {
    companion object : IntEntityClass<User>(Users) {
        fun findByUsername(username: String): User? {
            return User.find { Users.username eq username }.firstOrNull()
        }
    }

    var username by Users.username
    var password by Users.password
    val player by Player backReferencedOn Players.user
}

