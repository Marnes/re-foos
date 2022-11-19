package com.epifoos.domain.player

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.stats.PlayerStat
import com.epifoos.domain.stats.PlayerStats
import com.epifoos.domain.user.User
import com.epifoos.domain.user.Users
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Players : BaseIntIdTable("player") {
    var user = reference("user_id", Users)
        .uniqueIndex("player_user_uniq_idx")
}

class Player(id: EntityID<Int>) : BaseIntEntity(id, Players) {
    companion object : IntEntityClass<Player>(Players)

    var user by User referencedOn Players.user
    val stats by PlayerStat backReferencedOn PlayerStats.player
    //val global by lazy { GlobalEntity.findById(id.value)!! } LAZY LOAD
}


