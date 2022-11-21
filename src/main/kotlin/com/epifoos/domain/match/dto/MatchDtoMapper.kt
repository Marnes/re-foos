package com.epifoos.domain.match.dto

import com.epifoos.domain.league.dto.LeagueDtoMapper
import com.epifoos.domain.match.Game
import com.epifoos.domain.match.Match
import com.epifoos.domain.match.Team
import com.epifoos.domain.player.dto.PlayerDtoMapper
import com.epifoos.domain.stats.GamePlayerStats
import com.epifoos.domain.stats.GameStats
import com.epifoos.domain.stats.MatchPlayerStats
import com.epifoos.domain.stats.MatchStats
import com.epifoos.domain.stats.dto.MatchPlayerStatsDtoMapper
import com.epifoos.domain.user.UserDtoMapper

object MatchDtoMapper {

    fun map(
        match: Match,
        games: List<Game>,
        matchStats: MatchStats,
        matchPlayerStats: List<MatchPlayerStats>,
        gameStatsMap: Map<Game, GameStats>,
        gamePlayerStatsMap: Map<Game, List<GamePlayerStats>>
    ): MatchDto {
        val initialEloMap = matchPlayerStats.associateBy({ it.player }, { it.initialElo })

        return MatchDto(
            match.id.value,
            match.createdDate,
            UserDtoMapper.map(match.createdBy),
            LeagueDtoMapper.map(match.league),
            games.map { mapGame(it, gameStatsMap[it]!!, gamePlayerStatsMap[it]!!) },
            matchPlayerStats.associateBy(
                { it.player.id.value },
                { PlayerDtoMapper.mapMinified(it.player, initialEloMap[it.player]!!) }),
            MatchPlayerStatsDtoMapper.mapMatchStats(matchPlayerStats, matchStats),
            matchStats.winner?.id?.value,
            matchStats.loser?.id?.value,
        )
    }

    private fun mapGame(
        game: Game,
        gameStats: GameStats,
        gamePlayerStats: List<GamePlayerStats>,
    ): GameDto {
        return GameDto(
            MatchPlayerStatsDtoMapper.mapGameStats(gamePlayerStats),
            game.teams.map { mapTeam(it, gameStats) },
            gameStats.winner?.id?.value,
            gameStats.loser?.id?.value,
        )
    }

    private fun mapTeam(team: Team, gameStats: GameStats): TeamDto {
        return TeamDto(
            team.id.value,
            team.scores.map { it.score },
            team.players.map { it.id.value },
            gameStats.winner == team,
            gameStats.loser == team
        )
    }
}
