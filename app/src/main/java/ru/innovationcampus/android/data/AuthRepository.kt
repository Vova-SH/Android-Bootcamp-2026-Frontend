package ru.innovationcampus.android.data

import ru.innovationcampus.android.data.source.AuthLocalDataSource
import ru.innovationcampus.android.data.source.AuthNetworkDataSource

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) {
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