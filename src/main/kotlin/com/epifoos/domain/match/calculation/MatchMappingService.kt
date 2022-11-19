package com.epifoos.domain.match.calculation

import com.epifoos.domain.match.*
import com.epifoos.domain.match.MatchResult.*
import com.epifoos.domain.match.calculation.mapper.GameMapper
import com.epifoos.domain.match.calculation.mapper.MatchMapper
import com.epifoos.domain.player.Player
import kotlin.math.pow

//TODO: Get better word for Score (as score doesn't represent game SCORE but a calculated score)
//TODO: Replace hard coded values
object MatchMappingService {

    private const val ELO_WEIGHT = 400.0F
    private const val MAX_SCORE = 5

    fun create(match: Match, initialEloMap: Map<Player, Float>): MatchMapper {
        val players = initialEloMap.keys.toList();

        return MatchMapper(match, players, match.games.map { createForGame(it, initialEloMap) })
    }

    private fun createForGame(game: Game, initialEloMap: Map<Player, Float>): GameMapper {
        val playerTeamMap = getPlayerTeams(game)

        return GameMapper(
            game,
            getExpectedScoreMap(game, playerTeamMap, initialEloMap),
            getActualScoreMap(game, playerTeamMap),
            getResultMap(game, playerTeamMap)
        )
    }

    private fun getExpectedScoreMap(
        game: Game,
        playerTeamMap: Map<Player, Team>,
        initialEloMap: Map<Player, Float>
    ): Map<Player, Float> {
        val teamAverageMap = getTeamAverageElo(game, initialEloMap);

        return playerTeamMap.entries.associateBy({ it.key }, { getExpectedScore(it.value, teamAverageMap) })
    }

    private fun getActualScoreMap(
        game: Game,
        playerTeamMap: Map<Player, Team>
    ): Map<Player, Float> {
        val teamTotalScoreMap = getTeamTotalScore(game)

        return playerTeamMap.entries.associateBy({ it.key }, { getActualScore(it.value, teamTotalScoreMap) })
    }

    private fun getResultMap(
        game: Game,
        playerTeamMap: Map<Player, Team>
    ): Map<Player, Float> {
        val teamTotalWins = getTeamTotalWins(game)
        val teamTotalScore = getTeamTotalScore(game)

        return playerTeamMap.entries.associateBy({ it.key }, { getResultScore(it.value, teamTotalWins, teamTotalScore) })
    }

    private fun getExpectedScore(
        team: Team,
        teamAverageMap: Map<Team, Float>
    ): Float {
        val teamAverage = teamAverageMap[team]!!
        val otherAverage = teamAverageMap
            .filter { it.key != team }
            .map { it.value }
            .average()
            .toFloat()

        return estimateScoreVersus(teamAverage, otherAverage)
    }

    private fun getActualScore(team: Team, teamTotalScoreMap: Map<Team, Int>): Float {
        val teamTotalScore = teamTotalScoreMap[team]!!
        val otherAverageTotalScore = teamTotalScoreMap
            .filter { it.key != team }
            .map { it.value }
            .average()
            .toFloat()

        return normalizeScore(teamTotalScore, otherAverageTotalScore)
    }

    private fun getResultScore(team: Team, teamTotalWins: Map<Team, Int>, teamTotalScoreMap: Map<Team, Int>): Float {
        return getResult(team, teamTotalWins, teamTotalScoreMap).weight / MatchResult.max().weight
    }

    private fun getResult(team: Team, teamTotalWins: Map<Team, Int>, teamTotalScoreMap: Map<Team, Int>): MatchResult {
        val wins = teamTotalWins[team]!!
        val totalScore = teamTotalScoreMap[team]!!

        val win = teamTotalWins.filter { it.key != team }
            .all { wins > it.value }

        val draw = teamTotalWins.filter { it.key != team }
            .all { wins == it.value }

        val scoreWin = teamTotalScoreMap.filter { it.key != team }
            .all { totalScore > it.value }

        val scoreDraw = teamTotalScoreMap.filter { it.key != team }
            .all { totalScore == it.value }

        return if (win) {
            WIN
        } else if (draw && scoreWin) {
            WIN_ON_SCORE
        } else if (draw && scoreDraw) {
            DRAW
        } else if (draw) {
            LOSE_ON_SCORE
        } else {
            LOSE
        }
    }

    private fun getTeamAverageElo(game: Game, initialEloMap: Map<Player, Float>): Map<Team, Float> {
        return game.teams.associateWith { calculateAverageElo(it, initialEloMap) }
    }

    private fun getTeamTotalScore(game: Game): Map<Team, Int> {
        return game.teams.associateWith { team -> team.scores.sumOf { it.score } }
    }

    private fun getTeamTotalWins(game: Game): Map<Team, Int> {
        return game.teams.associateWith { getNumberOfWins(game, it) }
    }

    private fun getPlayerTeams(game: Game): Map<Player, Team> {
        val playerTeams = mutableMapOf<Player, Team>()
        val teamPlayers = game.teams.associateWith { team -> team.teamPlayers.map { it.player } }

        teamPlayers.forEach { teamPlayer -> teamPlayer.value.forEach { playerTeams[it] = teamPlayer.key } }

        return playerTeams
    }

    private fun calculateAverageElo(team: Team, initialEloMap: Map<Player, Float>): Float {
        return team.teamPlayers.map { initialEloMap[it.player]!! }
            .average()
            .toFloat()
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

    private fun estimateScoreVersus(teamAverageElo: Float, opponentAverageElo: Float): Float {
        return 1 / (1 + 10F.pow((opponentAverageElo - teamAverageElo) / ELO_WEIGHT)) //TODO: Replace 10F
    }

    private fun normalizeScore(teamTotalScore: Int, opponentAverageScore: Float): Float {
        return (teamTotalScore - opponentAverageScore + MAX_SCORE) / (2.0F * MAX_SCORE) //TODO: Replace 2F
    }
}

