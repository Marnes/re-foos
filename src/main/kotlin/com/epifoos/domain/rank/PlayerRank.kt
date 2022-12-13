package com.epifoos.domain.rank

import com.epifoos.domain.BaseIntEntity
import com.epifoos.domain.BaseIntIdTable
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

abstract class BasePlayerRankTable(name: String) : BaseIntIdTable(name) {
    var player = reference("player_id", PlayerTable)
    var rank = integer("rank")
}

abstract class BasePlayerRank(id: EntityID<Int>, table: BasePlayerRankTable) : BaseIntEntity(id, table) {
    var player by Player referencedOn table.player
    var rank by table.rank
}

object PlayerRankTable : BasePlayerRankTable("player_rank") {
}

class PlayerRank(id: EntityID<Int>) : BasePlayerRank(id, PlayerRankTable) {
    companion object : IntEntityClass<PlayerRank>(PlayerRankTable)
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
