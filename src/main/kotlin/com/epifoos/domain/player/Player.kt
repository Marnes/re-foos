package com.epifoos.domain.player

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueTable
import com.epifoos.domain.stats.PlayerStats
import com.epifoos.domain.stats.PlayerStatsTable
import com.epifoos.domain.user.User
import com.epifoos.domain.user.UserTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object PlayerTable : BaseIntIdTable("player") {
    var user = reference("user_id", UserTable)
    var league = reference("league_id", LeagueTable)

    init {
        uniqueIndex("player_user_league_uniq_idx", user, league)
    }
}

class Player(id: EntityID<Int>) : BaseIntEntity(id, PlayerTable) {
    companion object : IntEntityClass<Player>(PlayerTable)

    var user by User referencedOn PlayerTable.user
    var league by League referencedOn PlayerTable.league
    val stats by PlayerStats backReferencedOn PlayerStatsTable.player
}


object PlayerSpotlightTable: BaseIntIdTable("player_spotlight") {
    var player = reference("player_id", PlayerTable)
}

class PlayerSpotlight(id: EntityID<Int>) : BaseIntEntity(id, PlayerSpotlightTable) {
    companion object : IntEntityClass<PlayerSpotlight>(PlayerSpotlightTable)

    var player by Player referencedOn PlayerSpotlightTable.player
}
