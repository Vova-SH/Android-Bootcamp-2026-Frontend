package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun checkAndAuth(
        login: String,
        password: String,
    ): Result<Boolean> {
        authLocalDataSource.setToken(login, password)
        return authNetworkDataSource.checkAuth(
            authLocalDataSource.token ?: return Result.success(false)
        )
            .onSuccess { isLogin ->
                if (!isLogin) authLocalDataSource.clearToken()
            }
            .onFailure {
                authLocalDataSource.clearToken()
            }
    }


}