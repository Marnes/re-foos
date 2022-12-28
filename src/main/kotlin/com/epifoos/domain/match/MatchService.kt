package com.epifoos.domain.match

import com.epifoos.domain.match.dto.MatchDto
import com.epifoos.domain.match.dto.MatchDtoMapper
import com.epifoos.domain.rank.PlayerRankService
import com.epifoos.domain.stats.MatchPlayerStatsService
import com.epifoos.domain.stats.MatchStatsService
import com.epifoos.domain.stats.PlayerStatsSnapshotService
import com.epifoos.exceptions.EntityNotFoundException
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction


object MatchService {

    fun getMatch(matchId: Int): MatchDto {
        return transaction {
            val match = Match.findById(matchId)
                ?.load(Match::games, Match::createdBy, Match::league)
                ?: throw EntityNotFoundException("Could not find Match with ID $matchId")

            val games = match.games.with(Game::teams, Team::players).toList()
            val players = games.flatMap { it.teams }.flatMap { it.players }.toSet()

            val matchPlayerStats = MatchPlayerStatsService.getMatchPlayerStats(match).associateBy { it.player }
            val matchPlayerStatsSnapshots = PlayerStatsSnapshotService.getStatsSnapshot(match).associateBy { it.player }
            val matchPlayerRankSnapshots = PlayerRankService.getRankSnapshots(match).associateBy { it.player }
            val gamePlayerStats = MatchPlayerStatsService.getGamePlayerStats(games)
            val matchStats = MatchStatsService.getMatchStats(match)
            val gameStatsMap = MatchStatsService.getGameStats(games)

            MatchDtoMapper.map(
                match,
                games,
                players,
                matchStats,
                matchPlayerStats,
                matchPlayerStatsSnapshots,
                matchPlayerRankSnapshots,
                gameStatsMap,
                gamePlayerStats
            )
        }
    }
}
