package com.epifoos.domain.calculation.data.types

import com.epifoos.domain.calculation.MatchResult
import com.epifoos.domain.calculation.data.DataMapper
import com.epifoos.domain.calculation.data.dto.GameData
import com.epifoos.domain.calculation.data.dto.MatchData
import com.epifoos.domain.match.*
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerUtil

//TODO: Refactor class
class RoundRobinDataMapper : DataMapper() {
    companion object {
        fun create(): RoundRobinDataMapper {
            return RoundRobinDataMapper()
        }
    }

    override fun getMatchData(match: Match): MatchData {
        val players = MatchUtil.getPlayers(match)
        val initialEloMap = PlayerUtil.getEloMap(players)
        val gameData = match.games.associateWith { getGameData(it, initialEloMap) }

        val scoreForMap = getScoreForMap(players, gameData.values)
        val scoreAgainstMap = getScoreAgainstMap(players, gameData.values)
        val gameWinsMap = getGamesWinsMap(players, gameData.values)
        val gameLossesMap = getGamesLossMap(players, gameData.values)
        val roundWinsMap = getRoundWinsMap(players, gameData.values)
        val roundLossesMap = getRoundLossesMap(players, gameData.values)
        val winner = getWinner(gameWinsMap)
        val loser = getLoser(gameLossesMap)

        return MatchData(
            players,
            winner,
            loser,
            scoreForMap,
            scoreAgainstMap,
            gameWinsMap,
            gameLossesMap,
            roundWinsMap,
            roundLossesMap,
            gameData
        )
    }

    private fun getWinner(gameWinsMap: Map<Player, Int>): Set<Player> {
        val playersByWins = gameWinsMap.entries.groupBy({ it.value }, { it.key })
        val highestWins = playersByWins.keys.max()
        val highestWinsPlayers = playersByWins[highestWins]!!

        if (highestWinsPlayers.size == 1) {
            return highestWinsPlayers.toSet()
        }

        return setOf()
    }

    private fun getLoser(gameLossesMap: Map<Player, Int>): Set<Player> {
        val playerByLosses = gameLossesMap.entries.groupBy({ it.value }, { it.key })
        val highestLosses = playerByLosses.keys.max()
        val highestLossesPlayers = playerByLosses[highestLosses]!!

        if (highestLossesPlayers.size == 1) {
            return highestLossesPlayers.toSet()
        }

        return setOf()
    }

    private fun getScoreForMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.sumOf { it.getTotalScore(player) } }
    }

    private fun getScoreAgainstMap(players: Set<Player>, gameData: Collection<GameData>): Map<Player, Int> {
        return players.associateWith { player -> gameData.sumOf { it.getOpponentTotalScore(player) } }
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
        val playerTeams = getPlayerTeamsMap(game)
        val totalPlayed = getTotalPlayedMap(game)
        val totalScores = getTotalScoreMap(game)
        val scoresAgainst = getScoreAgainstMap(game)
        val totalWins = getTotalWinsMap(game)
        val totalLosses = getTotalLossesMap(game)
        val winner = getWinResult(totalWins, totalScores)
        val loser = getLoseResult(totalWins, totalScores)
        val resultMap = getResultMap(game, winner, loser)
        val teamAverageElo = getTeamAverageEloMap(game, initialEloMap)

        return GameData(
            playerTeams,
            initialEloMap,
            winner.first,
            loser.first,
            totalPlayed,
            totalScores,
            scoresAgainst,
            totalWins,
            totalLosses,
            resultMap,
            teamAverageElo
        )
    }

    private fun getPlayerTeamsMap(game: Game): Map<Player, Team> {
        val playerTeams = mutableMapOf<Player, Team>()
        val teamPlayers = game.teams.associateWith { team -> team.teamPlayers.map { it.player } }

        teamPlayers.forEach { teamPlayer -> teamPlayer.value.forEach { playerTeams[it] = teamPlayer.key } }

        return playerTeams
    }

    private fun getWinResult(teamTotalWins: Map<Team, Int>, teamTotalScores: Map<Team, Int>): Pair<Team?, MatchResult> {
        val teamsByWins = teamTotalWins.entries.groupBy({ it.value }, { it.key })
        val highestWins = teamsByWins.keys.max()
        val highestWinsTeams = teamsByWins[highestWins]!!

        val teamsByHighestScore = teamTotalScores.entries
            .filter { highestWinsTeams.contains(it.key) }
            .groupBy({ it.value }, { it.key })

        val highestScore = teamsByHighestScore.keys.max()
        val highestScoreTeams = teamsByHighestScore[highestScore]!!


        if (highestWinsTeams.size == 1) {
            return Pair(highestWinsTeams.first(), MatchResult.WIN)
        } else if (highestScoreTeams.size == 1) {
            return Pair(highestScoreTeams.first(), MatchResult.WIN_ON_SCORE)
        }

        return Pair(null, MatchResult.DRAW)
    }

    private fun getLoseResult(
        teamTotalWins: Map<Team, Int>,
        teamTotalScores: Map<Team, Int>
    ): Pair<Team?, MatchResult> {
        val teamsByWins = teamTotalWins.entries.groupBy({ it.value }, { it.key })
        val lowestWins = teamsByWins.keys.min()
        val lowestWinsTeams = teamsByWins[lowestWins]!!

        val teamsByLowestScore = teamTotalScores.entries
            .filter { lowestWinsTeams.contains(it.key) }
            .groupBy({ it.value }, { it.key })

        val lowestScore = teamsByLowestScore.keys.min()
        val lowestScoreTeams = teamsByLowestScore[lowestScore]!!

        if (lowestWinsTeams.size == 1) {
            return Pair(lowestWinsTeams.first(), MatchResult.LOSE)
        } else if (lowestScoreTeams.size == 1) {
            return Pair(lowestScoreTeams.first(), MatchResult.LOSE_ON_SCORE)
        }

        return Pair(null, MatchResult.DRAW)
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

    private fun getTotalWinsMap(game: Game): Map<Team, Int> {
        return game.teams.associateWith { getNumberOfWins(game, it) }
    }

    private fun getNumberOfWins(game: Game, team: Team): Int {
        return team.scores.filterIndexed { index, score -> isHighestScore(game, team, score, index) }
            .count()
    }

    private fun isHighestScore(game: Game, team: Team, score: TeamScore, index: Int): Boolean {
        return game.teams.filter { it != team }
            .map { it.scores.elementAt(index) }
            .map { it.score }
            .none { it > score.score }
    }

    private fun getTotalLossesMap(game: Game): Map<Team, Int> {
        return game.teams.associateWith { getNumberOfLosses(game, it) }
    }

    private fun getNumberOfLosses(game: Game, team: Team): Int {
        return team.scores.filterIndexed { index, score -> isLowestScore(game, team, score, index) }
            .count()
    }

    private fun isLowestScore(game: Game, team: Team, score: TeamScore, index: Int): Boolean {
        return game.teams.filter { it != team }
            .map { it.scores.elementAt(index) }
            .map { it.score }
            .all { it < score.score }
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
        return team.teamPlayers.map { initialEloMap[it.player]!! }
            .average()
            .toFloat()
    }
}
