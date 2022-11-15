package com.epifoos.match

import com.epifoos.elo.EloService
import com.epifoos.game.Game
import com.epifoos.league.League
import com.epifoos.player.Player
import com.epifoos.user.User
import com.epifoos.user.Users
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object MatchService {

    fun captureMatch(matchSubmission: MatchSubmission, currentUser: User) {
        transaction {
            val users = User.find { Users.username inList matchSubmission.players }
                .with(User::player)
                .with(Player::stats)
                .map { it.player }
                .associateBy { it.user.username }

            val match = Match.new {
                league = League.all().first()
                createdBy = currentUser
            }
            matchSubmission.games.map {
                Game.new {
                    leftPlayer1 = users[it.leftPlayer1]!!
                    leftPlayer2 = users[it.leftPlayer2]!!
                    rightPlayer1 = users[it.rightPlayer1]!!
                    rightPlayer2 = users[it.rightPlayer2]!!
                    leftScore1 = it.leftScore1
                    leftScore2 = it.leftScore2
                    rightScore1 = it.rightScore1
                    rightScore2 = it.rightScore2
                    this.match = match
                }
            }

            EloService.updateElo(match)
        }
    }
}
