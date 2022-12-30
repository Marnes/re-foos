package com.epifoos.domain.match

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.GameSubmissionDto
import com.epifoos.domain.match.dto.MatchSubmissionDto
import com.epifoos.domain.match.dto.TeamSubmissionDto
import com.epifoos.domain.match.validation.MatchValidationService
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.user.User
import com.epifoos.exceptions.AuthorizationException
import com.epifoos.exceptions.EntityNotFoundException
import com.epifoos.exceptions.ValidationException
import io.konform.validation.Invalid
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

object MatchSubmissionService {

    fun captureMatch(leagueId: Int, matchSubmissionDto: MatchSubmissionDto, currentUser: User) {
        transaction {
            val league = League.findById(leagueId)?.load(League::config)
                ?: throw EntityNotFoundException("Could not find League with ID $leagueId")

            if (Player.find { PlayerTable.user eq currentUser.id and(PlayerTable.league eq leagueId) }.empty()) {
                throw AuthorizationException("Cannot capture match for league you are not part of")
            }

            if (league.isClosed()) {
                throw AuthorizationException("League is closed");
            }

            val validationResult = MatchValidationService.validate(league, matchSubmissionDto)

            if (validationResult is Invalid) {
                throw ValidationException(validationResult.errors)
            }

            val match = createMatch(league, matchSubmissionDto, currentUser)
            MatchEngine.calculate(league, match)
        }
    }

    private fun createMatch(league: League, matchSubmissionDto: MatchSubmissionDto, currentUser: User): Match {
        val players = Player.find { PlayerTable.id inList matchSubmissionDto.getPlayers() }
            .with(Player::rank, Player::stats)
            .associateBy { it.id.value }

        return Match.new {
            this.league = league
            createdBy = currentUser
        }.also { match -> matchSubmissionDto.games.map { createGame(match, players, it) } }
    }

    private fun createGame(match: Match, playerMap: Map<Int, Player>, gameSubmissionDto: GameSubmissionDto) {
        Game.new { this.match = match }
            .also { game -> gameSubmissionDto.teams.forEach { createTeam(game, it, playerMap) } }
    }

    private fun createTeam(
        game: Game,
        teamSubmissionDto: TeamSubmissionDto,
        playerMap: Map<Int, Player>
    ) {
        Team.new {
            this.game = game
            players = SizedCollection(teamSubmissionDto.players.map { playerMap[it]!! })
        }.also { team -> teamSubmissionDto.scores.forEach { createTeamScore(team, it) } }
    }

    private fun createTeamScore(team: Team, score: Int) {
        TeamScore.new {
            this.team = team
            this.score = score
        }
    }
}
