package com.epifoos.auth

import com.epifoos.exceptions.AuthenticationException
import com.epifoos.user.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.transactions.transaction

object AuthUtil {
    fun getCurrentUser(call: ApplicationCall): User {
        return transaction {
            User.findById(getId(call))!!
        }
    }

    private fun getId(call: ApplicationCall): Int {
        return when (val principal = call.principal<Principal>()) {
            is UserIdPrincipal -> principal.name.toInt()
            is JWTPrincipal -> principal.payload.getClaim("id").asInt()
            else -> throw AuthenticationException()
        }
    }
}
