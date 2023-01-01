package com.epifoos.domain.match

import com.epifoos.domain.league.LeagueContext
import com.epifoos.domain.league.LeagueContextHelper
import com.epifoos.domain.match.dto.GameSubmissionDto
import com.epifoos.domain.match.dto.MatchSubmissionDto
import com.epifoos.domain.match.dto.TeamSubmissionDto
import com.epifoos.domain.match.validation.MatchValidationService
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerLeagueRepository
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.player.PlayerWrapper
import com.epifoos.domain.user.User
import com.epifoos.exceptions.AuthorizationException
import com.epifoos.exceptions.ValidationException
import io.konform.validation.Invalid
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

object MatchSubmissionService {

    fun captureMatch(leagueId: Int, matchSubmissionDto: MatchSubmissionDto, currentUser: User) {
        transaction {
            val leagueContext = LeagueContextHelper.getActive(leagueId)
            val players = PlayerLeagueRepository.findByIds(matchSubmissionDto.getPlayers(), leagueContext)

            if (Player.find { PlayerTable.user eq currentUser.id and (PlayerTable.league eq leagueContext.leagueId()) }
                    .empty()) {
                throw AuthorizationException("Cannot capture match for league you are not part of")
            }

            if (leagueContext.league.isClosed()) {
                throw AuthorizationException("League is closed");
            }

            val validationResult = MatchValidationService.validate(leagueContext.league, matchSubmissionDto)

            if (validationResult is Invalid) {
                throw ValidationException(validationResult.errors)
            }

            val match = createMatch(leagueContext, matchSubmissionDto, currentUser)
            MatchEngine.calculate(
                MatchCalculationSubmission(
                    leagueContext,
                    match,
                    players,
                    MatchSubmissionHelper.getInitialEloMap(leagueContext.league, players)
                )
            )
        }
    }

    private fun createMatch(
        leagueContext: LeagueContext,
        matchSubmissionDto: MatchSubmissionDto,
        currentUser: User
    ): Match {
        val players = PlayerLeagueRepository.findByIds(matchSubmissionDto.getPlayers(), leagueContext)
            .associateBy { it.player.id.value }

        return Match.new {
            this.league = leagueContext.league
            this.season = leagueContext.season
            createdBy = currentUser
        }.also { match -> matchSubmissionDto.games.map { createGame(match, players, it) } }
    }

    private fun createGame(match: Match, playerMap: Map<Int, PlayerWrapper>, gameSubmissionDto: GameSubmissionDto) {
        Game.new { this.match = match }
            .also { game -> gameSubmissionDto.teams.forEach { createTeam(game, it, playerMap) } }
    }

    private fun createTeam(
        game: Game,
        teamSubmissionDto: TeamSubmissionDto,
        playerMap: Map<Int, PlayerWrapper>
    ) {
        Team.new {
            this.game = game
            players = SizedCollection(teamSubmissionDto.players.map { playerMap[it]!!.player })
        }.also { team -> teamSubmissionDto.scores.forEach { createTeamScore(team, it) } }
    }

    private fun createTeamScore(team: Team, score: Int) {
        TeamScore.new {
            this.team = team
            this.score = score
        }
    }
}
