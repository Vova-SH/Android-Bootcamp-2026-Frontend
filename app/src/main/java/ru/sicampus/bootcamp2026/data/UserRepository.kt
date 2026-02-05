package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.PagingUserListEntity
import ru.sicampus.bootcamp2026.domain.entities.UserEntity


class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUsers(
        page: Int,
        count: Int
    ): Result<PagingUserListEntity> {
        return userInfoDataSource.getUser(
            page = page,
            size = count
        ).mapCatching { dto ->
            PagingUserListEntity(
                isLast = dto.last ?: true,
                users = dto.content?.mapNotNull { userDto ->
                    UserEntity(
                        name = userDto.name ?: return@mapNotNull null,
                        photoUrl = userDto.photoUrl ?: return@mapNotNull null,
                        email = userDto.email ?: return@mapNotNull null,
                        phone = userDto.phone ?: return@mapNotNull null,
                        info = userDto.info ?: return@mapNotNull null,
                    )
                } ?: error("List is null")
            )
        }
    }

    suspend fun getCurrentUser(): Result<UserEntity>? {
        return userInfoDataSource.getCurrentUser().map { currentUser ->
            UserEntity(
                name = currentUser.name ?: return null,
                phone = currentUser.phone ?: return null,
                email = currentUser.email ?: return null,
                photoUrl = currentUser.photoUrl ?: return null,
                info = currentUser.info ?: return null,
            )

        }
    }

    suspend fun updateCurrentUser(
        name: String?,
        phone: String?,
        email: String?,
        info: String?,
        photoUrl: String?
    ): Result<Unit>{
        return userInfoDataSource.updateCurrentUser(name, phone, email, info, photoUrl).map {
        }
    }
}

