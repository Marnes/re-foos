package com.epifoos.player

import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerService {

    fun getPlayers(): List<PlayerDTO> {
        return transaction {
            Player.all().with(Player::user).with(Player::stats).map { PlayerDTO.fromPlayer(it) }
        }
    }
}
