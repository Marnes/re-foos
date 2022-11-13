package com.epifoos.player

import com.epifoos.base.BaseEntity
import com.epifoos.base.BaseTable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column

object Players : BaseTable<String>("player") {
    override val id: Column<EntityID<String>> = reference("username", PlayerStats)
        .uniqueIndex("player_username_uniq_idx")

    var password = varchar("password", 255)
    var avatar = varchar("avatar", 255)
}

class Player(id: EntityID<String>) : BaseEntity<String>(id, Players) {
    companion object : EntityClass<String, Player>(Players) {
        fun createNewPlayer(username: String, hashedPassword: String): Player {
            return Player.new(username) {
                password = hashedPassword
                avatar = ""
                stats = PlayerStat.createDefaults(username)
            }
        }
    }

    var password by Players.password
    var avatar by Players.avatar
    var stats by PlayerStat referencedOn Players.id
}

data class PlayerDTO(
    val username: String,
    val elo: Float,
    val played: Int,
    val wins: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsForAvg: Float,
    val goalsAgainst: Int,
    val goalsAgainstAvg: Float,
    val eloChange: Float,
) {
    companion object {
        fun fromPlayer(player: Player): PlayerDTO {
            return PlayerDTO(
                player.id.value,
                player.stats.elo,
                player.stats.played,
                player.stats.wins,
                player.stats.losses,
                player.stats.goalsFor,
                if (player.stats.played == 0) 0F else player.stats.goalsFor.toFloat() / player.stats.played,
                player.stats.goalsAgainst,
                if (player.stats.played == 0) 0F else player.stats.goalsAgainst.toFloat() / player.stats.played,
                player.stats.eloChange
            )
        }
    }
}
