package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.AuthNetworkDataSource

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
            .mapCatching { personResponse ->
                // Сохраняем ID пользователя из principal
                authLocalDataSource.setUserId(personResponse.id)
                true
            }
            .onFailure {
                authLocalDataSource.clearToken()
            }
    }
}