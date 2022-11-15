package com.epifoos.auth

import com.epifoos.config.Config
import com.epifoos.exceptions.AuthenticationException
import com.epifoos.player.Player
import com.epifoos.player.PlayerStat
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
        val secret = Config.getProperty("ktor.secret")!!

        if (registerRequest.secret != secret) {
            throw AuthenticationException()
        }

        val user = transaction {
            User.new {
                username = registerRequest.username
                password = BCrypt.hashpw(registerRequest.password, BCrypt.gensalt())
            }.also {
                Player.new {
                    user = it
                    avatar = ""
                }.also { PlayerStat.createDefaults(it) }
            }
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
}
