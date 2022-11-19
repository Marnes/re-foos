package com.epifoos.domain.auth

data class UserAuthResponse(
    val username: String,
    val jwt: String
)
