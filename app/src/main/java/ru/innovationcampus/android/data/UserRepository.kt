package ru.innovationcampus.android.data

import ru.innovationcampus.android.data.source.UserInfoDataSource
import ru.innovationcampus.android.domain.list.entities.PagingUserListEntity
import ru.innovationcampus.android.domain.list.entities.UserEntity

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
                        avatarUrl = userDto.avatarUrl ?: return@mapNotNull null,
                        email = userDto.email ?: return@mapNotNull null,
                    )
                } ?: error("List is null")
            )
        }
    }
}