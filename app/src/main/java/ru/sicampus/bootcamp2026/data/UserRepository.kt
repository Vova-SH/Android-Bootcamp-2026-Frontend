package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class UserRepository(
    private val userInfoDataSource: UserInfoDataSource,
    val getUser: Any
)
{
    suspend fun getUsers(): Result<List<UserEntity>>{
        return  userInfoDataSource.getUser().map{ listDto ->
            listDto.mapNotNull { userDto ->
                UserEntity(
                    name = userDto.name ?: return@mapNotNull null,
                    photoUrl = userDto.photoUrl ?: return@mapNotNull null,
                    email = userDto.email ?: return@mapNotNull null,
                )
            }

        }
    }
}