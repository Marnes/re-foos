package com.epifoos.auth

import com.epifoos.user.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.transactions.transaction

object AuthUtil {
    fun getCurrentUser(call: ApplicationCall): User {
        return transaction {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            User.findById(id)!!
        }
    }
}
