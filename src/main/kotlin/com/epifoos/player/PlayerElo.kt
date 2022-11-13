package com.epifoos.player

import com.epifoos.base.BaseIntEntity
import com.epifoos.base.BaseIntIdTable
import com.epifoos.match.Match
import com.epifoos.match.Matches
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerElos : BaseIntIdTable("player_elo") {

    var player = reference("player", Players)
    var match = reference("match_id", Matches)
    var change = float("change")
    var elo = float("elo")

    fun findLatestElos(playerIds: List<String>): List<PlayerElo> {
        val latestElos = mutableListOf<PlayerElo>()
        transaction {
            PlayerElos.run {
                select {
                    id inSubQuery slice(id.max()).selectAll().groupBy(player).having { player inList playerIds }
                }
            }
        }
        return latestElos
    }
}

class PlayerElo(id: EntityID<Int>) : BaseIntEntity(id, PlayerElos) {
    companion object : IntEntityClass<PlayerElo>(PlayerElos)

    var player by Player referencedOn PlayerElos.player
    var match by Match referencedOn PlayerElos.match
    var change by PlayerElos.change
    var elo by PlayerElos.elo
}
