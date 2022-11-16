package com.epifoos.player

import com.epifoos.player.stats.StatsService
import com.epifoos.user.User
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerService {

    fun createPlayer(user: User): Player {
        return transaction {
            Player.new {
                this.user = user
                avatar = ""
            }.also { StatsService.createDefault(it) }
        }
    }

    fun getPlayers(): List<PlayerDTO> {
        return transaction {
            Player.all().with(Player::user).with(Player::stats).map { PlayerDTO.fromPlayer(it) }
        }
    }
}
