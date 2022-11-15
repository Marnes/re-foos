package com.epifoos.player

import com.epifoos.user.User
import com.epifoos.user.Users
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerService {

    fun getPlayers(): List<PlayerDTO> {
        return transaction {
            User.all().with(User::player).with(Player::stats).map { PlayerDTO.fromPlayer(it) }
        }
    }

    fun getPlayers(usernames: List<String>): List<PlayerDTO> {
        return transaction {
            User.find { Users.username inList usernames }.with(User::player).with(Player::stats).map { PlayerDTO.fromPlayer(it) }
        }
    }
}
