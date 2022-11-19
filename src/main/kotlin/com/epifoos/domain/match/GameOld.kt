package com.epifoos.game

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.Matches
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.Players
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object GamesOld : BaseIntIdTable("game_old") {
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

class GameOld(id: EntityID<Int>) : BaseIntEntity(id, GamesOld) {
    companion object : IntEntityClass<GameOld>(GamesOld) {
        const val MAX_SCORE = 10
    }

    var match by Match referencedOn GamesOld.match
    var leftPlayer1 by Player referencedOn GamesOld.leftPlayer1
    var leftPlayer2 by Player referencedOn GamesOld.leftPlayer2
    var rightPlayer1 by Player referencedOn GamesOld.rightPlayer1
    var rightPlayer2 by Player referencedOn GamesOld.rightPlayer2
    var leftScore1 by GamesOld.leftScore1
    var leftScore2 by GamesOld.leftScore2
    var rightScore1 by GamesOld.rightScore1
    var rightScore2 by GamesOld.rightScore2

}
