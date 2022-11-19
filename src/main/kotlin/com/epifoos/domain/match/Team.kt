package com.epifoos.domain.match

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object TeamTable : BaseIntIdTable(("team")) {
    var game = reference("game_id", GameTable)
}

class Team(id: EntityID<Int>) : BaseIntEntity(id, TeamTable) {
    companion object : IntEntityClass<Team>(TeamTable)

    var game by Game referencedOn TeamTable.game
    val scores by TeamScore referrersOn TeamScoreTable.team
    val teamPlayers by TeamPlayer referrersOn TeamPlayerTable.team
}

object TeamScoreTable : BaseIntIdTable(("team_scores")) {
    var team = reference("team_id", TeamTable)
    var score = integer("score")
}

class TeamScore(id: EntityID<Int>) : BaseIntEntity(id, TeamScoreTable) {
    companion object : IntEntityClass<TeamScore>(TeamScoreTable)

    var team by Team referencedOn TeamScoreTable.team
    var score by TeamScoreTable.score
}

object TeamPlayerTable : BaseIntIdTable(("team_player")) {
    var team = reference("team_id", TeamTable)
    var player = reference("player_id", PlayerTable)

    init {
        uniqueIndex("team_player_unique_idx", team, player)
    }
}

class TeamPlayer(id: EntityID<Int>) : BaseIntEntity(id, TeamPlayerTable) {
    companion object : IntEntityClass<TeamPlayer>(TeamPlayerTable)

    var team by Team referencedOn TeamPlayerTable.team
    var player by Player referencedOn TeamPlayerTable.player
}
