package com.epifoos.domain.rank

import com.epifoos.domain.Elo
import com.epifoos.domain.league.League
import com.epifoos.domain.match.Match
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerRankService {

    fun updateRanks(league: League, match: Match) {
        saveSnapshot(league, match)
        rankPlayers(league)
    }

    fun resetRanks(players: List<Player>, matches: List<Match>) {
        val playerIds = players.map { it.id }
        val matchIds = matches.map { it.id }

        transaction {
            PlayerRankTable.deleteWhere { player inList playerIds }
            MatchPlayerRankSnapshotTable.deleteWhere { match inList matchIds }
        }
    }

    fun getRankSnapshots(match: Match): List<MatchPlayerRankSnapshot> {
        return MatchPlayerRankSnapshot.find { MatchPlayerRankSnapshotTable.match eq match.id }
            .with(MatchPlayerRankSnapshot::player)
            .toList()
    }

    private fun rankPlayers(league: League) {
        transaction {
            val players = getPlayers(league)
                .filter { it.stats.played > 0 }
                .sortedByDescending { it.stats.elo }

            rankPlayers(players)
        }
    }

    private fun saveSnapshot(league: League, match: Match) {
        transaction {
            getPlayers(league)
                .filter { it.rank != null }
                .map { it.rank!! }
                .forEach { createSnapshot(it, match) }
        }
    }

    private fun rankPlayers(players: List<Player>) {
        var currentRank = 0
        var currentElo: Elo? = null

        val playerRankMap = players.associateWith { it.rank }

        players.forEach {
            if (currentElo == null || currentElo != it.stats.elo) {
                currentRank++
            }

            updatePlayerRank(it, currentRank, playerRankMap)
            currentElo = it.stats.elo
        }
    }

    private fun updatePlayerRank(player: Player, rank: Int, playerRankMap: Map<Player, PlayerRank?>) {
        val playerRank = playerRankMap[player] ?: PlayerRank.new { this.player = player }
        playerRank.rank = rank
    }

    private fun getPlayers(league: League): List<Player> {
        return Player.find { PlayerTable.league eq league.id }.with(Player::stats, Player::rank, Player::user).with(Player::rank, Player::stats).toList()
    }

    private fun createSnapshot(playerRank: PlayerRank, match: Match) {
        MatchPlayerRankSnapshot.new {
            this.match = match
            player = playerRank.player
            rank = playerRank.rank
        }
    }
}
