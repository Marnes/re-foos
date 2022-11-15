package com.epifoos.auth

data class ChangePasswordRequest(

    val oldPassword: String,
    val newPassword: String,

)
