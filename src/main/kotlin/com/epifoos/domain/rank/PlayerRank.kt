package com.epifoos.domain.rank

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.league.League
import com.epifoos.domain.league.LeagueSeason
import com.epifoos.domain.league.LeagueSeasonTable
import com.epifoos.domain.league.LeagueTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and

abstract class BasePlayerRankTable(name: String) : BaseIntIdTable(name) {
    var player = reference("player_id", PlayerTable)
    var rank = integer("rank")
}

abstract class BasePlayerRank(id: EntityID<Int>, table: BasePlayerRankTable) : BaseIntEntity(id, table) {
    var player by Player referencedOn table.player
    var rank by table.rank
}

object PlayerRankTable : BasePlayerRankTable("player_rank") {
    var league = reference("league_id", LeagueTable)
    var season = reference("league_season_id", LeagueSeasonTable)

    init {
        uniqueIndex("player_rank_league_season_unique_idx", player, league, season)
    }
}

class PlayerRank(id: EntityID<Int>) : BasePlayerRank(id, PlayerRankTable) {
    companion object : IntEntityClass<PlayerRank>(PlayerRankTable) {
        fun findByPlayerAndSeason(playerId: Int, leagueSeasonId: Int): PlayerRank? {
            return PlayerRank.find { PlayerRankTable.player eq playerId and (PlayerRankTable.season eq leagueSeasonId) }
                .firstOrNull()
        }

        fun findByPlayersAndSeason(playerIds: Collection<Int>, leagueSeasonId: Int): SizedIterable<PlayerRank> {
            return PlayerRank.find { PlayerRankTable.player inList playerIds and (PlayerRankTable.season eq leagueSeasonId) }
                .with(PlayerRank::player, PlayerRank::season)
        }

        fun findByLeagueSeason(leagueSeasonId: Int): SizedIterable<PlayerRank> {
            return PlayerRank.find { PlayerRankTable.season eq leagueSeasonId }
                .with(PlayerRank::player, PlayerRank::season)
        }
    }

    var league by League referencedOn PlayerRankTable.league
    var season by LeagueSeason referencedOn PlayerRankTable.season
}

object MatchPlayerRankSnapshotTable : BasePlayerRankTable("match_player_rank_snapshot") {
    var match = reference("match_id", MatchTable)

    init {
        uniqueIndex("match_player_rank_unique_idx", match, player)
    }
}

class MatchPlayerRankSnapshot(id: EntityID<Int>) : BasePlayerRank(id, MatchPlayerRankSnapshotTable) {
    companion object : IntEntityClass<MatchPlayerRankSnapshot>(MatchPlayerRankSnapshotTable)

    var match by Match referencedOn MatchPlayerRankSnapshotTable.match
}
