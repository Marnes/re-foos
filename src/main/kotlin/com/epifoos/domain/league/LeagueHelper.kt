package com.epifoos.domain.league

import com.epifoos.domain.match.MatchTable
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.PlayerTable
import com.epifoos.domain.rank.PlayerRank
import com.epifoos.domain.rank.PlayerRankTable
import com.epifoos.domain.user.User
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.util.*

object LeagueHelper {

    fun getUserLeagueIds(user: User?): List<Int> {
        if (user == null) {
            return listOf()
        }

        return PlayerTable.slice(PlayerTable.league).select { PlayerTable.user eq user.id }
            .map { it[PlayerTable.league].value }
    }

    fun getLeagueLeader(leagueSeason: LeagueSeason): Player? {
        return PlayerRank.find { PlayerRankTable.season eq leagueSeason.id and (PlayerRankTable.rank eq 1) }.limit(1)
            .firstOrNull()?.player
    }

    fun getLeaguesLeaders(leagueSeasons: Collection<LeagueSeason>): Map<LeagueSeason, Player> {
        return PlayerRank.find { PlayerRankTable.season inList leagueSeasons.map { it.id } and (PlayerRankTable.rank eq 1) }
            .with(PlayerRank::season, PlayerRank::player).associateBy({ it.season }, { it.player })
    }

    fun generateUid(): String {
        return UUID.randomUUID().toString().substring(0, 7)
    }

    fun getLatestMatchId(leagueContext: LeagueContext): Int? {
        return (MatchTable)
            .slice(MatchTable.id)
            .select { MatchTable.season eq leagueContext.seasonId() }
            .orderBy(MatchTable.createdDate to SortOrder.DESC)
            .limit(1)
            .firstOrNull()
            ?.get(MatchTable.id)
            ?.value
    }
}
