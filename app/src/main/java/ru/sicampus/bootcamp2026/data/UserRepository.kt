package ru.sicampus.bootcamp2026.data

import android.view.PixelCopy
import kotlinx.coroutines.delay
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.add.entities.PagingUserListEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

class UserRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<UserEntity>{
        return authNetworkDataSource.register(email, password, fullName).mapCatching { userDto ->
            UserEntity(
                id = userDto.id ?: throw Exception("ID is null"),
                email = userDto.email ?: throw Exception("Email is null"),
                fullName = userDto.fullName ?: throw Exception("FullName is null")
            )
        }
    }

    suspend fun getUsers(page: Int, size: Int): Result<PagingUserListEntity>{
        delay(2_000)
        if(Math.random() > 0.8) return Result.failure(IllegalStateException("Ops"))
        return userInfoDataSource.getUsers(page = page, size = size).mapCatching{ dto ->
            PagingUserListEntity(
                isLast = dto.last ?: true,
                users = dto.content?.mapNotNull { userDto ->
                    UserEntity(
                        id = userDto.id ?: return@mapNotNull null,
                        email = userDto.email ?: return@mapNotNull null,
                        fullName = userDto.fullName ?: return@mapNotNull null,
                    )
                } ?: error("List is null")
            )
        }
    }

    suspend fun changeUser(
        id: Int,
        email: String,
        fullname: String
    ): Result<Unit> {
        return userInfoDataSource.changeUser(id, email,fullname)
    }
}