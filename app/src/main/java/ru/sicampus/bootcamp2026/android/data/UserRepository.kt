package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.android.domain.entities.UserEntity
import kotlin.collections.mapNotNull

class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUsers(): Result<List<UserEntity>> {
        return userInfoDataSource.getUser().map { listDto ->
            listDto.mapNotNull { UserDTO ->
                UserEntity(
                    name = UserDTO.fullName ?: return@mapNotNull null,
                    email = UserDTO.email ?: return@mapNotNull null,
                )
            }
        }
    }
}