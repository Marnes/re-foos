package com.epifoos.domain.player.dto

import com.epifoos.domain.player.Player
import com.epifoos.domain.rank.BasePlayerRank
import com.epifoos.domain.stats.AbstractPlayerStats

object PlayerDtoMapper {

    fun map(player: Player, rank: BasePlayerRank?, stats: AbstractPlayerStats?): PlayerDto {
        return PlayerDto(
            player.id.value,
            player.createdDate,
            player.user.id.value,
            player.user.username,
            player.user.avatar,
            rank?.rank,
            stats?.elo,
            stats?.played,
            stats?.wins,
            stats?.losses,
            stats?.eloChange,
            stats?.highestElo,
            stats?.lowestElo,
            stats?.scoreFor,
            stats?.scoreAgainst,
            stats?.winningStreak,
            stats?.losingStreak,
            stats?.longestWinningStreak,
            stats?.longestLosingStreak,
        )
    }

    fun mapMinified(player: Player): PlayerMinifiedDto {
        return PlayerMinifiedDto(
            player.id.value,
            player.createdDate,
            player.user.id.value,
            player.user.username,
            player.user.avatar,
        )
    }

}
