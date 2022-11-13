package com.epifoos.elo

import com.epifoos.player.Player
import com.epifoos.game.Game
import com.epifoos.match.Match
import com.epifoos.player.PlayerElo
import com.epifoos.player.StatsService
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.pow

object EloService {
    private const val STARTING_CHANGE = 0.0F
    private const val ELO_WEIGHT = 400.0F

    private const val RESULT_WEIGHT = 0.5F
    private const val GOAL_WEIGHT = 1.0F - RESULT_WEIGHT

    fun updateElo(match: Match) {
        val players = match.players
        val eloChanges = calculateMatchEloChanges(match)

        transaction {
            players.forEach {
                updatePlayerElo(match, it, eloChanges[it]!!)
                StatsService.updateStats(it, match, eloChanges[it]!!)
            }
        }
    }

    private fun updatePlayerElo(match: Match, player: Player, eloChange: Float) {
        val previousElo = player.stats.elo
        val updatedElo = previousElo + eloChange

        val playerElo = PlayerElo.new {
            this.player = player
            change = eloChange
            elo = updatedElo
        }

        playerElo.match = match
    }

    private fun calculateMatchEloChanges(match: Match): Map<Player, Float> {
        val initialEloChanges = match.players.associateWith { STARTING_CHANGE }
        return match.games.fold(initialEloChanges) { previousEloChanges, game ->
            calculateGameEloChanges(
                game,
                Match.Companion.Format.CompleteMatch
            ).map { it.key to it.value + previousEloChanges[it.key]!! }.toMap()
        }
    }

    private fun calculateGameEloChanges(game: Game, format: Match.Companion.Format): Map<Player, Float> {
        val leftEloAverage = (game.leftPlayer1.stats.elo + game.leftPlayer2.stats.elo) / 2
        val rightEloAverage = (game.rightPlayer1.stats.elo + game.rightPlayer2.stats.elo) / 2

        val leftExpectedScore = estimateScoreVersus(leftEloAverage, rightEloAverage)
        val rightExpectedScore = 1.0F - leftExpectedScore

        val resultBasedEloChanges = calculateResultBasedEloChanges(game, format, leftExpectedScore, rightExpectedScore)
        val goalBasedEloChanges = calculateGoalBasedEloChanges(game, format, leftExpectedScore, rightExpectedScore)

        return game.players.associateWith {
            (resultBasedEloChanges[it]!! * RESULT_WEIGHT + goalBasedEloChanges[it]!! * GOAL_WEIGHT)
        }
    }

    private fun calculateResultBasedEloChanges(
        game: Game, format: Match.Companion.Format, leftExpectedScore: Float, rightExpectedScore: Float
    ): Map<Player, Float> {
        val leftActualScore = game.leftResult.weight / Game.Companion.Result.max().weight
        val rightActualScore = game.rightResult.weight / Game.Companion.Result.max().weight

        return calculateEloChanges(
            game, format, leftExpectedScore, leftActualScore, rightExpectedScore, rightActualScore
        )
    }

    private fun calculateGoalBasedEloChanges(
        game: Game, format: Match.Companion.Format, leftExpectedScore: Float, rightExpectedScore: Float
    ): Map<Player, Float> {
        fun normalizeScore(goalDifference: Int): Float {
            return (goalDifference + Game.MAX_SCORE) / (2.0F * Game.MAX_SCORE)
        }

        val leftGoalDifference = (game.leftTotal - game.rightTotal)
        val rightGoalDifference = (game.rightTotal - game.leftTotal)

        val leftActualScore = normalizeScore(leftGoalDifference)
        val rightActualScore = normalizeScore(rightGoalDifference)

        return calculateEloChanges(
            game, format, leftExpectedScore, leftActualScore, rightExpectedScore, rightActualScore
        )
    }

    private fun calculateEloChanges(
        game: Game,
        format: Match.Companion.Format,
        leftExpectedScore: Float,
        leftActualScore: Float,
        rightExpectedScore: Float,
        rightActualScore: Float
    ): Map<Player, Float> {
        return game.players.map {
            it to calculateEloChange(
                game, it, format, leftExpectedScore, leftActualScore, rightExpectedScore, rightActualScore
            )
        }.toMap()
    }

    private fun calculateEloChange(
        game: Game,
        player: Player,
        format: Match.Companion.Format,
        leftExpectedScore: Float,
        leftActualScore: Float,
        rightExpectedScore: Float,
        rightActualScore: Float
    ): Float {
        if (game.leftPlayers.contains(player)) {
            return format.kValue * (leftActualScore - leftExpectedScore)
        }

        return format.kValue * (rightActualScore - rightExpectedScore)
    }

    private fun estimateScoreVersus(playerElo: Float, opponentElo: Float): Float {
        return 1 / (1 + 10F.pow((opponentElo - playerElo) / ELO_WEIGHT))
    }
}
