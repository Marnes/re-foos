package com.epifoos.auth

import com.epifoos.exceptions.AuthenticationException
import com.epifoos.player.PlayerService
import com.epifoos.security.JwtService
import com.epifoos.user.User
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object AuthService {

    fun login(loginRequest: LoginRequest): UserAuthResponse {
        var user: User = transaction { User.findByUsername(loginRequest.username) ?: throw AuthenticationException() }

        if (!BCrypt.checkpw(loginRequest.password, user.password)) {
            throw AuthenticationException()
        }

        return UserAuthResponse(
            user.username,
            JwtService.generateJwt(user)
        )
    }

    fun register(registerRequest: RegisterRequest): UserAuthResponse {
        val user = transaction {
            User.new {
                username = registerRequest.username
                password = BCrypt.hashpw(registerRequest.password, BCrypt.gensalt())
            }.also { PlayerService.createPlayer(it) }
        }

        return UserAuthResponse(
            user.username,
            JwtService.generateJwt(user)
        )
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest, currentUser: User) {
        if (!BCrypt.checkpw(changePasswordRequest.oldPassword, currentUser.password)) {
            throw AuthenticationException()
        }

        transaction {
            currentUser.password = BCrypt.hashpw(changePasswordRequest.newPassword, BCrypt.gensalt())
        }
    }

    fun authenticate(username: String, password: String): User {
        var user: User = transaction { User.findByUsername(username) ?: throw AuthenticationException() }

        if (!BCrypt.checkpw(password, user.password)) {
            throw AuthenticationException()
        }

        return user
    }
}
