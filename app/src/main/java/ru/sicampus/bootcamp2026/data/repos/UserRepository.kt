package ru.sicampus.bootcamp2026.data.repos

import ru.sicampus.bootcamp2026.data.mappers.toDomain
import ru.sicampus.bootcamp2026.data.source.remote.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.models.User

class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUsers(): Result<List<User>> {
        return userInfoDataSource.getUser().map { listDto ->
            listDto.mapNotNull { userDto -> userDto.toDomain() }
        }
    }
}
