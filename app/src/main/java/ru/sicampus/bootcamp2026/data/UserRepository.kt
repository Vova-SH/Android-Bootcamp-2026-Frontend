package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

open class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    fun getUsers() {
        TODO("Not yet implemented")
    }

    companion object {
        suspend fun getUsers(userRepository: UserRepository): Result<List<UserEntity>> {
            return userRepository.userInfoDataSource.getUser().map { listDto ->
                listDto.map { userDto ->
                    UserEntity(
                        name = userDto.name,
                        photoUrl = userDto.photoUrl,
                        email = userDto.email,
                    )
                }
            }
        }
    }
}