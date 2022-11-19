package com.epifoos.domain.admin

import com.epifoos.domain.league.League
import com.epifoos.domain.match.calculation.CalculationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoutes() {
    route("/admin") {
        authenticate("basic") {
            post("/recalculate") {
                CalculationService.recalculate(League.findById(call.request.queryParameters["leagueId"]!!.toInt())!!)
                call.respond(HttpStatusCode.OK)
            }

            post("/migrate") {
                AdminService.migrate()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
