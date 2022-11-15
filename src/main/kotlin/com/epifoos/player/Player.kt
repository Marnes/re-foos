package com.epifoos.player

import com.epifoos.base.BaseIntEntity
import com.epifoos.base.BaseIntIdTable
import com.epifoos.user.User
import com.epifoos.user.Users
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Players : BaseIntIdTable("player") {
    var user = reference("user_id", Users)
        .uniqueIndex("player_user_uniq_idx")

    var avatar = varchar("avatar", 255)
}

class Player(id: EntityID<Int>) : BaseIntEntity(id, Players) {
    companion object : IntEntityClass<Player>(Players)

    var user by User referencedOn Players.user
    var avatar by Players.avatar
    val stats by PlayerStat backReferencedOn PlayerStats.player
    //val global by lazy { GlobalEntity.findById(id.value)!! } LAZY LOAD
}


