package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) {
    suspend fun login(login: String, password: String): Result<UserDto> {
        return authNetworkDataSource.login(login, password)
            .map { userDto ->
                authLocalDataSource.setToken(login, password)
                userDto.id?.let { userId ->
                    authLocalDataSource.setUserId(userId)
                }
                userDto
            }
    }

    suspend fun checkAuth(): Result<Boolean> {
        return authNetworkDataSource.checkAuth()
            .onSuccess { isLogin ->
                if (!isLogin) authLocalDataSource.clearToken()
            }
            .onFailure {
                authLocalDataSource.clearToken()
            }
    }


    suspend fun register(
        login: String,
        password: String,
        name: String,
        lastName: String,
        email: String,
        phoneNumber: String,
    ): Result<UserDto> {
        return authNetworkDataSource.register(
            login, password, name, lastName, email, phoneNumber
        ).map { userDto ->
            authLocalDataSource.setToken(login, password)
            userDto.id?.let { userId ->
                authLocalDataSource.setUserId(userId)
            }
            userDto
        }
    }

    suspend fun logout() {
        authLocalDataSource.clearToken()
    }
    suspend fun checkAndAuth(
        login: String,
        password: String,
    ): Result<Boolean> {
        authLocalDataSource.setToken(login, password)
        return authNetworkDataSource.checkAuth()
            .onSuccess { isLogin ->
                if (!isLogin) authLocalDataSource.clearToken()
            }
            .onFailure {
                authLocalDataSource.clearToken()
            }
    }
}
