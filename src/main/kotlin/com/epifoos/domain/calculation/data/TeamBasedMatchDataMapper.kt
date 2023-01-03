package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.Elo
import com.epifoos.domain.average
import com.epifoos.domain.calculation.data.MatchDataMapper
import com.epifoos.domain.calculation.data.dto.GameData
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.Team
import com.epifoos.domain.player.Player

open class TeamBasedMatchDataMapper<W : WinConditionMapper>(winConditionMapper: W) :
    MatchDataMapper<W>(winConditionMapper) {

    override fun getMatchData(league: League, match: Match, players: Set<Player>, initialEloMap: Map<Player, Elo>): MatchData {
        val gameData = match.games.associateWith { getGameData(league, it, initialEloMap) }
        val gameResults = gameData.map { it.value.gameResult }

        val totalScore = getTotalScore(gameData)
        val scoreForMap = getScoreForMap(players, gameData.values)
        val scoreAgainstMap = getScoreAgainstMap(players, gameData.values)

        val winner = winConditionMapper.getWinners(players, gameResults, scoreForMap)
        val loser = winConditionMapper.getLosers(players, gameResults, scoreForMap)

        return MatchData(
            players,
            initialEloMap,
            winner,
            loser,
            totalScore,
            scoreForMap,
            scoreAgainstMap,
            gameData
        )
    }

    private fun getTotalScore(gameData: Map<Game, GameData>): Int {
        return gameData.values.sumOf { it.totalScored }
    }

    private fun getScoreForMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.sumOf { it.getScoreFor(player) } }
    }

    private fun getScoreAgainstMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.sumOf { it.getScoreAgainst(player) } }
    }

    private fun getGameData(league: League, game: Game, initialEloMap: Map<Player, Elo>): GameData {
        val teams = getTeams(game)
        val playerTeams = getPlayerTeamsMap(game, teams)
        val totalPlayed = getTotalPlayedMap(game)
        val totalScores = getTotalScoreMap(game)
        val scoresAgainst = getScoreAgainstMap(game)
        val teamAverageElo = getTeamAverageEloMap(game, initialEloMap)

        val gameResult = winConditionMapper.getGameResult(league, game)

        return GameData(
            teams,
            playerTeams,
            gameResult,
            totalPlayed,
            totalScores.values.sum(),
            totalScores,
            scoresAgainst,
            teamAverageElo
        )
    }

    private fun getTeams(game: Game): Map<Team, List<Player>> {
        return game.teams.associateWith { team -> team.players.map { it } }
    }

    private fun getPlayerTeamsMap(game: Game, teamPlayers: Map<Team, List<Player>>): Map<Player, Team> {
        val playerTeams = mutableMapOf<Player, Team>()

        teamPlayers.forEach { teamPlayer -> teamPlayer.value.forEach { playerTeams[it] = teamPlayer.key } }

        return playerTeams
    }

    private fun getTotalScoreMap(game: Game): Map<Team, Int> {
        return game.teams.associateWith { team -> team.scores.sumOf { it.score } }
    }

    private fun getScoreAgainstMap(game: Game): Map<Team, Int> {
        return game.teams.associateWith { getScoreAgainst(game, it) }
    }

    private fun getScoreAgainst(game: Game, team: Team): Int {
        return game.teams.filter { it != team }
            .flatMap { it.scores }
            .sumOf { it.score }
    }

    private fun getTotalPlayedMap(game: Game): Int {
        return game.teams.first().scores.count().toInt()
    }

    private fun getTeamAverageEloMap(game: Game, initialEloMap: Map<Player, Elo>): Map<Team, Elo> {
        return game.teams.associateWith { calculateAverageElo(it, initialEloMap) }
    }

    private fun calculateAverageElo(team: Team, initialEloMap: Map<Player, Elo>): Elo {
        return team.players.map { initialEloMap[it]!! }
            .average()
    }

}
