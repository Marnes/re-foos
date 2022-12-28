package com.epifoos.domain.user

object UserDtoMapper {
    fun map(user: User): UserDto {
        return UserDto(user.id.value, user.username, user.avatar, user.createdDate, user.admin)
    }
}
