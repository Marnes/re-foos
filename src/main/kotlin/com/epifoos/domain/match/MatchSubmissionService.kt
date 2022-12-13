package com.epifoos.domain.match

import com.epifoos.domain.league.League
import com.epifoos.domain.match.dto.GameSubmissionDto
import com.epifoos.domain.match.dto.MatchSubmissionDto
import com.epifoos.domain.match.dto.TeamSubmissionDto
import com.epifoos.exceptions.ValidationException
import com.epifoos.domain.match.validation.MatchValidationService
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.user.User
import com.epifoos.exceptions.EntityNotFoundException
import io.konform.validation.Invalid
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction

object MatchSubmissionService {

    fun captureMatch(leagueId: Int, matchSubmissionDto: MatchSubmissionDto, currentUser: User) {
        val league = transaction { League.findById(leagueId)?.load(League::config) ?: throw EntityNotFoundException() }
        val validationResult = MatchValidationService.validate(league, matchSubmissionDto, currentUser)

        if (validationResult is Invalid) {
            throw ValidationException(validationResult.errors)
        }

        val match = transaction { createMatch(league, matchSubmissionDto, currentUser) }

        MatchEngine.calculate(league, match)
    }

    private fun createMatch(league: League, matchSubmissionDto: MatchSubmissionDto, currentUser: User): Match {
        val players = Player.find { PlayerTable.id inList matchSubmissionDto.players }
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
