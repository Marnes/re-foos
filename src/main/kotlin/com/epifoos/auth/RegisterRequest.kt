package com.epifoos.auth

data class RegisterRequest(

    val username: String,
    val password: String,
    val secret: String

)
