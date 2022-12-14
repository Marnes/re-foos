package com.epifoos.domain.league

import com.epifoos.domain.auth.AuthUtil
import com.epifoos.domain.highlight.HighlightService
import com.epifoos.domain.match.matchRoutes
import com.epifoos.domain.player.PlayerService
import com.epifoos.domain.player.playersRoutes
import com.epifoos.plugins.authorization.isAdmin
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.leagueRoutes() {
    route("/leagues") {
        authenticate {
            post {
                val league = LeagueService.createLeague(call.receive(), AuthUtil.authenticatedUser(call))
                call.respond(league)
            }
        }

        authenticate(optional = true) {
            get {
                call.respond(LeagueService.getLeagues(AuthUtil.optionalUser(call)))
            }
        }
    }

    route("/leagues/{leagueId}") {
        playersRoutes()
        matchRoutes()

        authenticate(optional = true) {
            get {
                call.respond(
                    LeagueService.getLeague(
                        LeagueContextHelper.getContext(call),
                        AuthUtil.optionalUser(call)
                    )
                )
            }
        }

        get("/leaderboard") {//TODO: Might need to split these to separate service
            call.respond(PlayerService.getPlayers(LeagueContextHelper.getContext(call)))
        }

        get("/highlights") {
            call.respond(HighlightService.getLatestHighlights(LeagueContextHelper.getContext(call)))
        }

        authenticate {
            post("/join") {
                call.respond(
                    LeagueService.joinLeague(
                        call.parameters["leagueId"]!!.toInt(),
                        AuthUtil.authenticatedUser(call),
                        call.request.queryParameters["code"]!!
                    )
                )
            }

            isAdmin {
                get("/codes") {
                    call.respond(LeagueService.getLeagueCode(call.parameters["leagueId"]!!.toInt()))
                }

                post("/codes") {
                    call.respond(LeagueService.generateNewCode(call.parameters["leagueId"]!!.toInt()))
                }

                post("/season") {
                    call.respond(
                        LeagueService.createNewSeason(
                            call.parameters["leagueId"]!!.toInt(),
                            AuthUtil.authenticatedUser(call)
                        )
                    )
                }
            }
        }
    }
}
