package com.epifoos.domain.match.dto

import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.Team
import com.epifoos.domain.player.Player
import com.epifoos.domain.player.dto.PlayerDtoMapper
import com.epifoos.domain.rank.MatchPlayerRankSnapshot
import com.epifoos.domain.stats.*
import com.epifoos.domain.stats.dto.MatchPlayerStatsDtoMapper
import com.epifoos.domain.user.UserDtoMapper

object MatchDtoMapper {

    fun map(
        match: Match,
        games: List<Game>,
        players: Set<Player>,
        matchStats: MatchStats,
        matchPlayerStats: Map<Player, MatchPlayerStats>,
        matchPlayerStatsSnapshots: Map<Player, MatchPlayerStatsSnapshot>,
        matchPlayerRankSnapshots: Map<Player, MatchPlayerRankSnapshot>,
        gameStatsMap: Map<Game, GameStats>,
        gamePlayerStatsMap: Map<Game, List<GamePlayerStats>>
    ): MatchDto {
        return MatchDto(
            match.id.value,
            match.createdDate,
            UserDtoMapper.map(match.createdBy),
            games.map { mapGame(it, players, gameStatsMap[it]!!, gamePlayerStatsMap[it]!!) },
            players.associateBy(
                { it.id.value },
                { PlayerDtoMapper.map(it, matchPlayerRankSnapshots[it], matchPlayerStatsSnapshots[it]) }),
            players.associateBy({ it.id.value }, { MatchPlayerStatsDtoMapper.mapMatchStats(matchPlayerStats[it]!!) }),
            matchStats.winners.map { it.id.value },
            matchStats.losers.map { it.id.value }
        )
    }

    private fun mapGame(
        game: Game,
        players: Set<Player>,
        gameStats: GameStats,
        gamePlayerStats: List<GamePlayerStats>,
    ): GameDto {
        val playerGameStats = gamePlayerStats.associateBy { it.player }

        return GameDto(
            players.associateBy({ it.id.value }, { MatchPlayerStatsDtoMapper.mapGameStats(playerGameStats[it]!!) }),
            game.teams.map { mapTeam(it) },
            gameStats.winner?.id?.value,
            gameStats.loser?.id?.value,
        )
    }

    private fun mapTeam(team: Team): TeamDto {
        return TeamDto(
            team.id.value,
            team.scores.map { it.score },
            team.players.map { it.id.value }
        )
    }
}
