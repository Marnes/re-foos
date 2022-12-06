package com.epifoos.domain.calculation.data.win

import com.epifoos.domain.calculation.MatchResult
import com.epifoos.domain.calculation.data.MatchDataMapper
import com.epifoos.domain.calculation.data.dto.GameData
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchUtil
import com.epifoos.domain.match.Team
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerUtil

open class DefaultMatchDataMapper<W : WinConditionMapper>(winConditionMapper: W) :
    MatchDataMapper<W>(winConditionMapper) {

    override fun getMatchData(match: Match): MatchData {
        val players = MatchUtil.getPlayers(match)
        val initialEloMap = PlayerUtil.getEloMap(players)
        val gameData = match.games.associateWith { getGameData(it, initialEloMap) }

        val totalScore = getTotalScore(gameData)
        val scoreForMap = getScoreForMap(players, gameData.values)
        val scoreAgainstMap = getScoreAgainstMap(players, gameData.values)
        val gameWinsMap = getGamesWinsMap(players, gameData.values)
        val gameLossesMap = getGamesLossMap(players, gameData.values)
        val roundWinsMap = getRoundWinsMap(players, gameData.values)
        val roundLossesMap = getRoundLossesMap(players, gameData.values)
        val winner = winConditionMapper.getWinner(gameWinsMap)
        val loser = winConditionMapper.getLoser(gameLossesMap)

        return MatchData(
            players,
            initialEloMap,
            winner,
            loser,
            totalScore,
            scoreForMap,
            scoreAgainstMap,
            gameWinsMap,
            gameLossesMap,
            roundWinsMap,
            roundLossesMap,
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

    private fun getGamesWinsMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.count { it.isWinner(player) } }
    }

    private fun getGamesLossMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.count { it.isLoser(player) } }
    }

    private fun getRoundWinsMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.sumOf { it.getWins(player) } }
    }

    private fun getRoundLossesMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.sumOf { it.getLosses(player) } }
    }

    private fun getGameData(game: Game, initialEloMap: Map<Player, Float>): GameData {
        val teams = getTeams(game)
        val playerTeams = getPlayerTeamsMap(game, teams)
        val totalPlayed = getTotalPlayedMap(game)
        val totalScores = getTotalScoreMap(game)
        val scoresAgainst = getScoreAgainstMap(game)
        val totalWins = winConditionMapper.getTotalWinsMap(game)
        val totalLosses = winConditionMapper.getTotalLossesMap(game)
        val winner = winConditionMapper.getWinResult(totalWins, totalScores)
        val loser = winConditionMapper.getLoseResult(totalWins, totalScores)
        val resultMap = getResultMap(game, winner, loser)
        val teamAverageElo = getTeamAverageEloMap(game, initialEloMap)

        return GameData(
            teams,
            playerTeams,
            winner.first,
            loser.first,
            totalPlayed,
            totalScores.values.sum(),
            totalScores,
            scoresAgainst,
            totalWins,
            totalLosses,
            resultMap,
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

    private fun getResultMap(
        game: Game,
        winner: Pair<Team?, MatchResult>,
        loser: Pair<Team?, MatchResult>
    ): Map<Team, MatchResult> {
        return game.teams.associateWith { mapOf(winner, loser)[it] ?: MatchResult.DRAW }
    }

    private fun getTeamAverageEloMap(game: Game, initialEloMap: Map<Player, Float>): Map<Team, Float> {
        return game.teams.associateWith { calculateAverageElo(it, initialEloMap) }
    }

    private fun calculateAverageElo(team: Team, initialEloMap: Map<Player, Float>): Float {
        return team.players.map { initialEloMap[it]!! }
            .average()
            .toFloat()
    }
}
