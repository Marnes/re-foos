package com.epifoos.domain.admin

import com.epifoos.domain.match.*
import com.epifoos.domain.player.Player
import com.epifoos.game.GameOld
import org.jetbrains.exposed.sql.transactions.transaction

object AdminService {

    fun migrate() {
        transaction {
            Match.all().sortedBy { it.createdDate }.forEach { migrate(it) }
        }
    }

    private fun migrate(match: Match) {
        match.gamesOld.forEach {
            val game = Game.new { this.match = match }
            createTeams(game, it)
        }
    }

    private fun createTeams(game: Game, gameOld: GameOld) {
        val team1 = Team.new { this.game = game }
        val team2 = Team.new { this.game = game }

        createScores(team1, gameOld.leftScore1, gameOld.leftScore2)
        createPlayers(team1, gameOld.leftPlayer1, gameOld.leftPlayer2)

        createScores(team2, gameOld.rightScore1, gameOld.rightScore2)
        createPlayers(team2, gameOld.rightPlayer1, gameOld.rightPlayer2)
    }

    private fun createScores(team: Team, score1: Int, score2: Int) {
        TeamScore.new {
            this.team = team
            score = score1
        }

        TeamScore.new {
            this.team = team
            score = score2
        }
    }

    private fun createPlayers(team: Team, player1: Player, player2: Player) {
        TeamPlayer.new {
            this.team = team
            player = player1
        }
        TeamPlayer.new {
            this.team = team
            player = player2
        }
    }
}
