package com.epifoos.domain.admin

import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueConfig
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.Team
import com.epifoos.domain.match.TeamScore
import com.epifoos.game.GameOld
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction

object AdminService {

    fun migrate() {
        transaction {
            Match.all().sortedBy { it.createdDate }.forEach { migrate(it) }
            League.all().forEach {
                LeagueConfig.new {
                    league = it
                    startingElo = 1000F
                }
            }
        }
    }

    private fun migrate(match: Match) {
        match.gamesOld.forEach {
            val game = Game.new { this.match = match }
            createTeams(game, it)
        }
    }

    private fun createTeams(game: Game, gameOld: GameOld) {
        val team1 = Team.new {
            this.game = game
            players = SizedCollection(listOf(gameOld.leftPlayer1, gameOld.leftPlayer2))
        }
        val team2 = Team.new {
            this.game = game
            players = SizedCollection(listOf(gameOld.rightPlayer1, gameOld.rightPlayer2))
        }

        createScores(team1, gameOld.leftScore1, gameOld.leftScore2)
        createScores(team2, gameOld.rightScore1, gameOld.rightScore2)
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
}
