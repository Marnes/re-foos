package com.epifoos.domain.user

import java.time.LocalDateTime

data class UserDto(
    val id: Int,
    val username: String,
    val avatar: String?,
    val createdDate: LocalDateTime,
    val admin: Boolean
)
