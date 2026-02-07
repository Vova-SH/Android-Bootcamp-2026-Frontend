package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private  val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun checkAndAuth(
        login: String,
        password: String,
    ): Result<UserDto> {
        authLocalDataSource.setToken(login,password)
        return authNetworkDataSource.checkAuth()
            .onSuccess { userDto ->
                userDto
            }
            .onFailure {
                authLocalDataSource.clearToken()
            }
    }
}