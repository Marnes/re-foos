package com.epifoos.domain.auth

import com.epifoos.domain.league.League
import com.epifoos.domain.player.PlayerService
import com.epifoos.domain.user.User
import com.epifoos.exceptions.AuthenticationException
import com.epifoos.security.JwtService
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object AuthService {

    fun login(loginRequest: LoginRequest): UserAuthResponse {
        val user: User = transaction {
            User.findByUsername(loginRequest.username) ?: throw AuthenticationException("Authentication exception")
        }

        if (!BCrypt.checkpw(loginRequest.password, user.password)) {
            throw AuthenticationException("Authentication exception")
        }

        return UserAuthResponse(
            user.username,
            JwtService.generateJwt(user)
        )
    }

    fun register(registerRequest: RegisterRequest): UserAuthResponse {
        val league = transaction { League.all().first() }

        val user = transaction {
            User.new {
                username = registerRequest.username
                password = BCrypt.hashpw(registerRequest.password, BCrypt.gensalt())
            }.also { PlayerService.createPlayer(league, it) }
        }

        return UserAuthResponse(
            user.username,
            JwtService.generateJwt(user)
        )
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest, currentUser: User) {
        if (!BCrypt.checkpw(changePasswordRequest.oldPassword, currentUser.password)) {
            throw AuthenticationException("Authentication exception")
        }

        transaction {
            currentUser.password = BCrypt.hashpw(changePasswordRequest.newPassword, BCrypt.gensalt())
        }
    }

    fun authenticate(username: String, password: String): User {
        var user: User =
            transaction { User.findByUsername(username) ?: throw AuthenticationException("Authentication exception") }

        if (!BCrypt.checkpw(password, user.password)) {
            throw AuthenticationException("Authentication exception")
        }

        return user
    }
}
