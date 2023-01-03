package com.epifoos.domain.league

import com.epifoos.domain.*
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object LeagueTable : AuditedTable("league") {
    var name = varchar("name", 255)
    var uid = varchar("uid", 10)
        .uniqueIndex("league_unique_uid")
}

class League(id: EntityID<Int>) : AuditedEntity(id, LeagueTable) {
    companion object : IntEntityClass<League>(LeagueTable)

    var name by LeagueTable.name
    var uid by LeagueTable.uid

    val seasons by LeagueSeason referrersOn LeagueSeasonTable.league
    val config by LeagueConfig backReferencedOn LeagueConfigTable.league
    val coefficients by LeagueCoefficients backReferencedOn LeagueCoefficientsTable.league


    fun getActiveOrLastSeason(): LeagueSeason {
        val now = LocalDate.now()

        return seasons.firstOrNull { it.endDate != null && (it.endDate!! == now || it.endDate!!.isAfter(now)) }
            ?: seasons.first { it.endDate == null }
    }
}

object LeagueSeasonTable : AuditedTable("league_season") {
    var league = reference("league_id", LeagueTable)
    var season = integer("season")
    var startDate = date("start_date")
    var endDate = date("end_date").nullable()

    init {
        uniqueIndex("league_season_unique_idx", league, season)
    }
}

class LeagueSeason(id: EntityID<Int>) : AuditedEntity(id, LeagueSeasonTable) {
    companion object : IntEntityClass<LeagueSeason>(LeagueSeasonTable) {
        fun find(leagueId: Int, season: Int?): LeagueSeason {
            val now = LocalDate.now()
            val seasons = LeagueSeason.find { LeagueSeasonTable.league eq leagueId }.toList()

            return seasons.firstOrNull { it.season == season }
                ?: seasons.firstOrNull { it.endDate != null && (it.endDate!! == now || it.endDate!!.isAfter(now)) }
                ?: seasons.first { it.endDate == null }
        }

        fun findActive(leagueId: Int): LeagueSeason? {
            val now = LocalDate.now()
            val seasons = LeagueSeason.find { LeagueSeasonTable.league eq leagueId }.toList()

            return seasons.firstOrNull { it.endDate != null && (it.endDate!! == now || it.endDate!!.isAfter(now)) }
                ?: seasons.first { it.endDate == null }
        }
    }

    var season by LeagueSeasonTable.season
    var league by League referencedOn LeagueSeasonTable.league
    var startDate by LeagueSeasonTable.startDate
    var endDate by LeagueSeasonTable.endDate


    fun isOpen(): Boolean {
        if (endDate == null) {
            return true
        }

        val today = LocalDate.now()

        return startDate == today ||
                endDate!! == today ||
                (startDate.isBefore(today) && endDate!!.isAfter(today))
    }
}

object LeagueConfigTable : BaseIntIdTable("league_config") {
    var league = reference("league_id", LeagueTable)
        .uniqueIndex("league_config_unique_idx")

    var startingElo = elo("starting_elo")
    var type = enumerationByName("type", 255, LeagueType::class)
    var games = integer("games")
    var teams = integer("teams")
    var players = integer("players")
    var scoresPerTeam = integer("scores_per_team")
    var playersPerTeam = integer("players_per_team")
    var maxScore = integer("max_score")
}

class LeagueConfig(id: EntityID<Int>) : BaseIntEntity(id, LeagueConfigTable) {
    companion object : IntEntityClass<LeagueConfig>(LeagueConfigTable)

    var league by League referencedOn LeagueConfigTable.league
    var startingElo by LeagueConfigTable.startingElo
    var type by LeagueConfigTable.type
    var games by LeagueConfigTable.games
    var teams by LeagueConfigTable.teams
    var players by LeagueConfigTable.players
    var scoresPerTeam by LeagueConfigTable.scoresPerTeam
    var playersPerTeam by LeagueConfigTable.playersPerTeam
    var maxScore by LeagueConfigTable.maxScore

    fun getNumberOfGames(): Int {
        if (type === LeagueType.ROUND_ROBIN) {
            return scoresPerTeam
        }

        return games
    }

    fun getGameMaxScore(): Int {
        if (type === LeagueType.ROUND_ROBIN) {
            return scoresPerTeam * maxScore
        }

        return maxScore;
    }


    fun getMinimumTotalScore(): Int {
        return getNumberOfGames() * maxScore
    }

}

object LeagueCoefficientsTable : BaseIntIdTable("league_coefficients") {
    var league = reference("league_id", LeagueTable)
        .uniqueIndex("league_coefficients_unique_idx")

    var kValue = double("k_value")
    var resultCoefficient = integer("result_coefficient")
}

class LeagueCoefficients(id: EntityID<Int>) : BaseIntEntity(id, LeagueCoefficientsTable) {
    companion object : IntEntityClass<LeagueCoefficients>(LeagueCoefficientsTable)

    var league by League referencedOn LeagueCoefficientsTable.league
    var kValue by LeagueCoefficientsTable.kValue
    var resultCoefficient by LeagueCoefficientsTable.resultCoefficient
}

enum class LeagueType {
    ROUND_ROBIN,
    HEAD_TO_HEAD
}
