package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class UserRepository(
    private val userInfoDataSource: UsersInfoDataSource
) {
    suspend fun getUsers(
        page: Int,
        size: Int
    ): Result<List<UserEntity>> {
        return userInfoDataSource.getUsers(
            page = page,
            size = size
        ).mapCatching { dto ->
            dto.content?.mapNotNull{ userDto ->
                UserEntity(
                    fullName = userDto.fullName ?: return@mapNotNull null,
                    jobTitle = userDto.jobTitle ?: return@mapNotNull null,
                    email = userDto.email ?: return@mapNotNull null,
                    avatarUrl = userDto.avatarUrl ?: return@mapNotNull null
                )
            } ?: error("List is null")
        }
    }
}



