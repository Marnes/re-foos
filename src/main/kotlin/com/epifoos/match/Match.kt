package com.epifoos.match

import com.epifoos.base.AuditedEntity
import com.epifoos.base.AuditedTable
import com.epifoos.game.Game
import com.epifoos.game.GameSubmission
import com.epifoos.game.Games
import com.epifoos.league.League
import com.epifoos.league.Leagues
import com.epifoos.player.Player
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


/**
 * A Match consists of multiple Games
 */
object Matches : AuditedTable(("match")) {
    var league = reference("league_id", Leagues)
}

class Match(id: EntityID<Int>) : AuditedEntity(id, Matches) {
    companion object : IntEntityClass<Match>(Matches) {
        enum class Format(val kValue: Float) {
            CompleteMatch(36.0F),
            SingleGame(12.0F);
        }
    }

    var league by League referencedOn Matches.league
    val games by Game referrersOn Games.match

    val players
        get() = listOf(
            games.first().leftPlayer1,
            games.first().leftPlayer2,
            games.first().rightPlayer1,
            games.first().rightPlayer2
        )

    fun getWinner(): Player? {
        val winners = players.filter { player -> games.all { it.isDraw() || it.getWinners().contains(player) } }

        if (winners.size == 1) {
            return winners.first()
        }

        return null
    }

    fun getLoser(): Player? {
        val losers = players.filter { player -> games.all { it.isDraw() || it.getLosers().contains(player) } }

        if (losers.size == 1) {
            return losers.first()
        }

        return null
    }
}

data class MatchSubmission(
    var games: List<GameSubmission>
) {
    val players
        get() = listOf(
            games.first().leftPlayer1,
            games.first().leftPlayer2,
            games.first().rightPlayer1,
            games.first().rightPlayer2
        )
}
