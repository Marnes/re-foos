package com.epifoos.player

import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerService {

    fun getPlayers(): List<PlayerDTO> {
        return transaction {
            Player.all().with(Player::stats).map { PlayerDTO.fromPlayer(it) }
        }
    }

    fun getPlayers(usernames: List<String>): List<PlayerDTO> {
        return transaction {
            Player.find { Players.id inList usernames }.with(Player::stats).map { PlayerDTO.fromPlayer(it) }
        }
    }
}
