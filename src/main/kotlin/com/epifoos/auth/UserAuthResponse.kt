package com.epifoos.auth

data class UserAuthResponse(
    val username: String,
    val jwt: String
)
