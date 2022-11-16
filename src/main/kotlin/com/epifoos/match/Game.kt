package com.epifoos.game

import com.epifoos.base.BaseIntEntity
import com.epifoos.base.BaseIntIdTable
import com.epifoos.match.Match
import com.epifoos.match.Matches
import com.epifoos.player.Player
import com.epifoos.player.Players
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Games : BaseIntIdTable("game") {
    var match = reference("match_id", Matches)
    var leftPlayer1 = reference("left_player_1", Players)
    var leftPlayer2 = reference("left_player_2", Players)
    var rightPlayer1 = reference("right_player_1", Players)
    var rightPlayer2 = reference("right_player_2", Players)
    var leftScore1 = integer("left_score_1")
    var leftScore2 = integer("left_score_2")
    var rightScore1 = integer("right_score_1")
    var rightScore2 = integer("right_score_2")
}

class Game(id: EntityID<Int>) : BaseIntEntity(id, Games) {
    companion object : IntEntityClass<Game>(Games) {
        const val MAX_SCORE = 5

        enum class Result(val weight: Float) {
            WIN_IN_TWO(4.0F),
            WIN_ON_SCORE(3.0F),
            DRAW(2.0F),
            LOSE_ON_SCORE(1.0F),
            LOSE_IN_TWO(0.0F);

            companion object {
                fun max(): Result = WIN_IN_TWO
            }
        }
    }

    var match by Match referencedOn Games.match
    var leftPlayer1 by Player referencedOn Games.leftPlayer1
    var leftPlayer2 by Player referencedOn Games.leftPlayer2
    var rightPlayer1 by Player referencedOn Games.rightPlayer1
    var rightPlayer2 by Player referencedOn Games.rightPlayer2
    var leftScore1 by Games.leftScore1
    var leftScore2 by Games.leftScore2
    var rightScore1 by Games.rightScore1
    var rightScore2 by Games.rightScore2

    val leftTotal get() = leftScore1 + leftScore2
    val rightTotal get() = rightScore1 + rightScore2

    val leftResult
        get() = if (leftTotal == MAX_SCORE) Result.WIN_IN_TWO
        else if (rightTotal == MAX_SCORE) Result.LOSE_IN_TWO
        else if (leftTotal > rightTotal) Result.WIN_ON_SCORE
        else if (leftTotal < rightTotal) Result.LOSE_ON_SCORE
        else Result.DRAW

    val rightResult
        get() = if (rightTotal == MAX_SCORE) Result.WIN_IN_TWO
        else if (leftTotal == MAX_SCORE) Result.LOSE_IN_TWO
        else if (rightTotal > leftTotal) Result.WIN_ON_SCORE
        else if (rightTotal < leftTotal) Result.LOSE_ON_SCORE
        else Result.DRAW

    val leftPlayers get() = listOf(leftPlayer1, leftPlayer2)
    val rightPlayers get() = listOf(rightPlayer1, rightPlayer2)
    val players get() = listOf(leftPlayer1, leftPlayer2, rightPlayer1, rightPlayer2)

    fun getWinners(): List<Player> {
        if (isDraw()) {
            return listOf()
        }

        return if (leftTotal > rightTotal) leftPlayers else rightPlayers;
    }

    fun getLosers(): List<Player> {
        if (isDraw()) {
            return listOf()
        }

        return if (leftTotal > rightTotal) rightPlayers else leftPlayers;
    }

    fun isDraw(): Boolean {
        return leftTotal == rightTotal
    }
}
