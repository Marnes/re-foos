package com.epifoos.domain.auth

data class ChangePasswordRequest(

    val oldPassword: String,
    val newPassword: String,

)
