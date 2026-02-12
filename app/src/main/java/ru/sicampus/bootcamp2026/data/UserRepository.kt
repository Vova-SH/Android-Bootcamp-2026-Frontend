package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.PagingUserListEntity
import ru.sicampus.bootcamp2026.domain.entities.UserEntity


class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUsers(
        page: Int,
        size: Int
    ): Result<PagingUserListEntity> {
        return userInfoDataSource.getUser(
            page = page,
            size = size,
        ).mapCatching { dto ->
            PagingUserListEntity(
                isLast = dto.last ?: true,
                users = dto.content?.mapNotNull { userDto ->
                    UserEntity(
                        name = userDto.name ?: return@mapNotNull null,
                        email = userDto.email ?: return@mapNotNull null,
                        id = userDto.id ?: return@mapNotNull null,
                        lastName = userDto.lastName ?: return@mapNotNull null,
                        login = userDto.login ?: return@mapNotNull null,
                        phoneNumber = userDto.phoneNumber ?: "",
                        department = userDto.department ?: return@mapNotNull null,
                        position = userDto.position ?: return@mapNotNull null,
                        photoUrl = userDto.photoUrl ?: return@mapNotNull null,
                    )
                } ?: error("List is null")
            )
        }
    }

    suspend fun getCurrentUser(): Result<UserEntity> {
        return userInfoDataSource.getCurrentUser().mapCatching { dto ->
            UserEntity(
                id = dto.id ?: 0,
                name = dto.name ?: "",
                lastName = dto.lastName ?: "",
                email = dto.email ?: "",
                login = dto.login ?: "",
                phoneNumber = dto.phoneNumber?: "",
                department = dto.department,
                position = dto.position,
                photoUrl = dto.photoUrl
            )
        }
    }
}