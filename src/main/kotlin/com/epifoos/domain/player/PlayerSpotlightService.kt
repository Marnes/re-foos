package com.epifoos.domain.player

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.time.LocalDate

object
PlayerSpotlightService {

    fun getSpotlight(leagueId: Int): Player {
        val playerSpotlightId = (PlayerSpotlightTable innerJoin PlayerTable)
            .slice(PlayerSpotlightTable.id)
            .select { PlayerTable.league eq leagueId }
            .orderBy(PlayerSpotlightTable.createdDate to SortOrder.DESC)
            .limit(1)
            .firstOrNull()

        if (playerSpotlightId == null) {
            return updateSpotlight(leagueId)
        } else {
            val playerSpotlight = PlayerSpotlight
                .find { PlayerSpotlightTable.id eq playerSpotlightId[PlayerSpotlightTable.id] }
                .first()

            if (playerSpotlight.createdDate < LocalDate.now().atStartOfDay()) {
                return updateSpotlight(leagueId)
            }

            return playerSpotlight.player
        }
    }

    //TODO: This should be run daily
    private fun updateSpotlight(leagueId: Int): Player {
        val playerIds = (PlayerSpotlightTable innerJoin PlayerTable)
            .slice(PlayerTable.id)
            .select { PlayerTable.league eq leagueId }
            .map { it[PlayerTable.id] }

        var player = Player.find { PlayerTable.id notInList playerIds }.shuffled().firstOrNull()

        if (player == null) {
            PlayerSpotlightTable.deleteWhere { PlayerSpotlightTable.player inList playerIds }
            player = Player.all().shuffled().firstOrNull()
        }

        PlayerSpotlight.new { this.player = player!! }

        return player!!
    }
}
