package com.epifoos.auth

import com.epifoos.player.Player
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.transactions.transaction

object AuthUtil {
    fun getCurrentUser(call: ApplicationCall): Player {
        return transaction {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()

            Player.findById(username)!!
        }
    }
}
