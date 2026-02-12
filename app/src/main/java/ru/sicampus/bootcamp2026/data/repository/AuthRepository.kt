package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.user.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.domain.entities.User
import ru.sicampus.bootcamp2026.domain.mapper.UserMapper
import ru.sicampus.bootcamp2026.utils.SettingsUtils

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val settingsUtils: SettingsUtils
) {

    suspend fun checkAndAuth(email: String, password: String): Result<Boolean>{
        authLocalDataSource.setToken(email, password)

        return authNetworkDataSource.checkAuth(
            authLocalDataSource.token ?: return Result.success(false),
            email, password, settingsUtils
        ).onSuccess { isLogin ->
            if (!isLogin) authLocalDataSource.clearToken()
        }.onFailure {
            authLocalDataSource.clearToken()
        }
    }

    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Result<User> {
        return authNetworkDataSource.register(email, password, firstName, secondName).map { userDto ->
            settingsUtils.setProfileData(userDto.id, email, password)
            UserMapper.toEntity(userDto)
        }
    }
}
