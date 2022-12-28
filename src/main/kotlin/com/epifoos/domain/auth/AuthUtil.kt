package com.epifoos.domain.auth

import com.epifoos.domain.user.User
import com.epifoos.exceptions.AuthenticationException
import io.ktor.server.application.*
import io.ktor.server.auth.*

object AuthUtil {
    fun authenticatedUser(call: ApplicationCall): User {
        return call.principal() ?: throw AuthenticationException("User not authenticated")
    }

    fun optionalUser(call: ApplicationCall): User? {
        return call.principal()
    }


}
