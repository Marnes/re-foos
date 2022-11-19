package com.epifoos.domain.player

import com.epifoos.domain.stats.StatsService
import com.epifoos.domain.user.User
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerService {

    fun createPlayer(user: User): Player {
        return transaction {
            Player.new {
                this.user = user
            }.also { StatsService.createDefault(it) }
        }
    }

    fun getPlayers(): List<PlayerDTO> {
        return transaction {
            Player.all().with(Player::user).with(Player::stats).map { PlayerDTO.fromPlayer(it) }
        }
    }
}
