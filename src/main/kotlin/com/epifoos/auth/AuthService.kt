package com.epifoos.auth

import com.epifoos.config.Config
import com.epifoos.player.Player
import com.epifoos.player.Players
import com.epifoos.plugins.AuthenticationException
import com.epifoos.security.JwtService
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object AuthService {

    fun login(loginRequest: LoginRequest): PlayerAuthResponse {
        var player: Player? = transaction {
            Player.find { Players.id eq loginRequest.username }.limit(1).firstOrNull()
        }

        if (player == null || !BCrypt.checkpw(loginRequest.password, player.password)) {
            throw AuthenticationException()
        }

        return PlayerAuthResponse(
            player.id.value,
            player.avatar,
            JwtService.generateJwt(player)
        )
    }

    fun register(registerRequest: RegisterRequest): PlayerAuthResponse {
        val secret = Config.getProperty("ktor.secret")!!

        if (registerRequest.secret != secret) {
            throw AuthenticationException()
        }

        val player = transaction {
            Player.createNewPlayer(
                registerRequest.username,
                BCrypt.hashpw(registerRequest.password, BCrypt.gensalt())
            )
        }

        return PlayerAuthResponse(
            player.id.value,
            player.avatar,
            JwtService.generateJwt(player)
        )
    }
}
