package com.epifoos.domain.match

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.GameSubmissionDto
import com.epifoos.domain.match.dto.MatchDto
import com.epifoos.domain.match.dto.MatchDtoMapper
import com.epifoos.domain.match.dto.MatchSubmissionDto
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.stats.MatchPlayerStatsService
import com.epifoos.domain.stats.MatchStatsService
import com.epifoos.domain.user.User
import com.epifoos.exceptions.EntityNotFoundException
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction


//Todo: Refactor
object MatchService {

    fun getMatch(matchId: Int): MatchDto? {
        return transaction {
            val match = Match.getCompleteMatch(matchId) ?: throw EntityNotFoundException()
            val games = match.games.toList()

            val matchPlayerStats = MatchPlayerStatsService.getMatchStats(match)
            val gamePlayerStats = MatchPlayerStatsService.getGameStats(games)
            val matchStats = MatchStatsService.getMatchStats(match)
            val gameStatsMap = MatchStatsService.getGameStats(games)

            MatchDtoMapper.map(match, games, matchStats, matchPlayerStats, gameStatsMap, gamePlayerStats)
        }
    }

    fun captureMatch(leagueId: Int, matchSubmissionDto: MatchSubmissionDto, currentUser: User) {
        transaction {
            val league = League.findById(leagueId)!!
            val players = Player.find { PlayerTable.id inList matchSubmissionDto.players }
                .with(Player::stats)
                .associateBy { it.id.value }

            val match = Match.new {
                this.league = league
                createdBy = currentUser
            }

            matchSubmissionDto.games.map { buildGame(match, players, it) }
            MatchEngine.doMagic(match)
        }
    }

    //TODO: Refactor to separate class
    private fun buildGame(match: Match, players: Map<Int, Player>, gameSubmissionDto: GameSubmissionDto) {
        Game.new { this.match = match }
            .also {
                buildTeam(
                    it,
                    players[gameSubmissionDto.leftPlayer1]!!,
                    players[gameSubmissionDto.leftPlayer2]!!,
                    gameSubmissionDto.leftScore1,
                    gameSubmissionDto.leftScore2
                )
            }
            .also {
                buildTeam(
                    it,
                    players[gameSubmissionDto.rightPlayer1]!!,
                    players[gameSubmissionDto.rightPlayer2]!!,
                    gameSubmissionDto.rightScore1,
                    gameSubmissionDto.rightScore2
                )
            }
    }

    private fun buildTeam(
        game: Game,
        player1: Player,
        player2: Player,
        score1: Int,
        score2: Int
    ) {
        Team.new {
            this.game = game
            players = SizedCollection(listOf(player1, player2))
        }.also { buildScore(it, score1, score2) }
    }

    private fun buildScore(team: Team, score1: Int, score2: Int) {
        TeamScore.new {
            this.team = team
            this.score = score1
        }
        TeamScore.new {
            this.team = team
            this.score = score2
        }
    }
}
