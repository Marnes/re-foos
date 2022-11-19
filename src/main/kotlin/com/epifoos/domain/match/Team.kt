package com.epifoos.domain.match

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.Players
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Teams : BaseIntIdTable(("team")) {
    var game = reference("game_id", Games)
}

class Team(id: EntityID<Int>) : BaseIntEntity(id, Teams) {
    companion object : IntEntityClass<Team>(Teams)

    var game by Game referencedOn Teams.game
    val scores by TeamScore referrersOn TeamScores.team
    val teamPlayers by TeamPlayer referrersOn TeamPlayers.team
}

object TeamScores : BaseIntIdTable(("team_scores")) {
    var team = reference("team_id", Teams)
    var score = integer("score")
}

class TeamScore(id: EntityID<Int>) : BaseIntEntity(id, TeamScores) {
    companion object : IntEntityClass<TeamScore>(TeamScores)

    var team by Team referencedOn TeamScores.team
    var score by TeamScores.score
}

object TeamPlayers : BaseIntIdTable(("team_player")) {
    var team = reference("team_id", Teams)
    var player = reference("player_id", Players)

    init {
        uniqueIndex("team_player_unique_idx", team, player)
    }
}

class TeamPlayer(id: EntityID<Int>) : BaseIntEntity(id, TeamPlayers) {
    companion object : IntEntityClass<TeamPlayer>(TeamPlayers)

    var team by Team referencedOn TeamPlayers.team
    var player by Player referencedOn TeamPlayers.player
}
