package com.epifoos.auth

data class PlayerAuthResponse(
    val username: String,
    val avatar: String,
    val jwt: String
)
