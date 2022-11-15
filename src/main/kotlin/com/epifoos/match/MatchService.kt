package com.epifoos.match

import com.epifoos.elo.EloService
import com.epifoos.game.Game
import com.epifoos.league.League
import com.epifoos.match.dto.MatchSubmissionDto
import com.epifoos.player.Player
import com.epifoos.player.Players
import com.epifoos.user.User
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object MatchService {

    fun captureMatch(matchSubmissionDto: MatchSubmissionDto, currentUser: User) {
        transaction {
            val players = Player.find { Players.id inList matchSubmissionDto.players }
                .with(Player::stats)
                .associateBy { it.id.value }

            val match = Match.new {
                league = League.all().first()
                createdBy = currentUser
            }

            matchSubmissionDto.games.map {
                Game.new {
                    leftPlayer1 = players[it.leftPlayer1]!!
                    leftPlayer2 = players[it.leftPlayer2]!!
                    rightPlayer1 = players[it.rightPlayer1]!!
                    rightPlayer2 = players[it.rightPlayer2]!!
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
