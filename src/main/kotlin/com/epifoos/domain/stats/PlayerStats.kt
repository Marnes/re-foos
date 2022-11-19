package com.epifoos.domain.stats

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.Matches
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.Players
import com.epifoos.game.GameOld
import com.epifoos.game.GamesOld
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

abstract class BasePlayerStats(name: String) : BaseIntIdTable(name) {
    var player = reference("player", Players)
    var eloChange = float("elo_change")
}

abstract class BasePlayerStat(id: EntityID<Int>, table: BasePlayerStats) : BaseIntEntity(id, table) {
    var player by Player referencedOn table.player
    var eloChange by table.eloChange
}

object PlayerStats : BasePlayerStats("player_stats") {
    var elo = float("elo")
    var played = integer("played")
    var wins = integer("wins")
    var losses = integer("losses")
    var scoreFor = integer("score_for")
    var scoreAgainst = integer("score_against")
    var winningStreak = integer("winning_streak")
    var losingStreak = integer("losing_streak")

    init {
        uniqueIndex("player_stats_unique_idx", player)
    }
}

class PlayerStat(id: EntityID<Int>) : BasePlayerStat(id, PlayerStats) {
    companion object : IntEntityClass<PlayerStat>(PlayerStats) {
        private const val DEFAULT_ELO = 1000F

        fun createDefaults(player: Player): PlayerStat {
            return PlayerStat.new {
                this.player = player
            }.also { it.setDefaults() }
        }
    }

    var elo by PlayerStats.elo
    var played by PlayerStats.played
    var wins by PlayerStats.wins
    var losses by PlayerStats.losses
    var scoreFor by PlayerStats.scoreFor
    var scoreAgainst by PlayerStats.scoreAgainst
    var winningStreak by PlayerStats.winningStreak
    var losingStreak by PlayerStats.losingStreak

    fun setDefaults() {
        elo = DEFAULT_ELO
        played = 0
        wins = 0
        losses = 0
        scoreFor = 0
        scoreAgainst = 0
        winningStreak = 0
        losingStreak = 0
        eloChange = 0F
    }
}

object MatchPlayerStats : BasePlayerStats("match_player_stats") {
    var match = reference("match_id", Matches)

    init {
        uniqueIndex("match_player_stats_unique_idx", match, player)
    }
}

class MatchPlayerStat(id: EntityID<Int>) : BasePlayerStat(id, MatchPlayerStats) {
    companion object : IntEntityClass<MatchPlayerStat>(MatchPlayerStats)

    var match by Match referencedOn MatchPlayerStats.match
}

object GamePlayerStats : BasePlayerStats("game_player_stats") {
    var game = reference("game_id", GamesOld)

    init {
        uniqueIndex("game_player_stats_unique_idx", game, player)
    }
}

class GamePlayerStat(id: EntityID<Int>) : BasePlayerStat(id, GamePlayerStats) {
    companion object : IntEntityClass<GamePlayerStat>(GamePlayerStats)

    var gameOld by GameOld referencedOn GamePlayerStats.game
}
