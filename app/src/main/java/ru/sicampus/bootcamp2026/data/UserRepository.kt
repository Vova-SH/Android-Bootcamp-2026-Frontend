package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.users.entities.PagingUserListEntity
import ru.sicampus.bootcamp2026.domain.users.entities.UserEntity

class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUser(
        id: Int
    ): Result<UserEntity> {
        return userInfoDataSource.getUser(id).mapCatching { dto ->
            dto.mapNotNull { userDto ->
                UserEntity(
                    id = userDto.id ?: return@mapNotNull null,
                    name = userDto.name ?: return@mapNotNull null,
                    photoUrl = userDto.photoUrl ?: return@mapNotNull null,
                    email = userDto.email ?: return@mapNotNull null,
                    surname = userDto.surname ?: return@mapNotNull null,
                    patronymic = userDto.patronymic ?: return@mapNotNull null,
                    username = userDto.username ?: return@mapNotNull null,
                    messengerLink = userDto.messengerLink ?: return@mapNotNull null,
                    phoneNumber = userDto.phoneNumber ?: return@mapNotNull null,
                    departmentName = userDto.departmentName ?: return@mapNotNull null,
                )
            }.first()
        }
    }
    suspend fun getUsers(
        page: Int,
        size: Int
    ): Result<PagingUserListEntity> {
        return userInfoDataSource.getUsers(
            page = page,
            size = size,
        ).mapCatching { dto ->
            PagingUserListEntity(
                isLast = dto.last ?: true,
                users = dto.content?.mapNotNull { userDto ->
                    UserEntity(
                        id = userDto.id ?: return@mapNotNull null,
                        name = userDto.name ?: return@mapNotNull null,
                        photoUrl = userDto.photoUrl ?: return@mapNotNull null,
                        email = userDto.email ?: return@mapNotNull null,
                        surname = userDto.surname ?: return@mapNotNull null,
                        patronymic = userDto.patronymic ?: return@mapNotNull null,
                        username = userDto.username ?: return@mapNotNull null,
                        messengerLink = userDto.messengerLink ?: return@mapNotNull null,
                        phoneNumber = userDto.phoneNumber ?: return@mapNotNull null,
                        departmentName = userDto.departmentName ?: return@mapNotNull null,
                    )
                } ?: error("List is null")
            )
        }
    }
}