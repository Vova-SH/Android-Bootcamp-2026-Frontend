package ru.sicampus.bootcamp2026.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sicampus.bootcamp2026.data.local.TokenDataStore
import ru.sicampus.bootcamp2026.data.mapper.toDomain
import ru.sicampus.bootcamp2026.data.remote.api.AuthApi
import ru.sicampus.bootcamp2026.data.remote.dto.LoginRequest
import ru.sicampus.bootcamp2026.data.remote.dto.RegisterRequest
import ru.sicampus.bootcamp2026.domain.model.AuthTokens
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Реализация репозитория для работы с аутентификацией
 */
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<AuthTokens> {
        return try {
            val response = authApi.register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
            val authTokens = response.toDomain()
            saveTokens(authTokens)
            Result.Success(authTokens)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthTokens> {
        return try {
            val response = authApi.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            val authTokens = response.toDomain()
            saveTokens(authTokens)
            Result.Success(authTokens)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun refresh(): Result<AuthTokens> {
        return try {
            val refreshToken = tokenDataStore.getRefreshTokenValue()
            if (refreshToken.isNullOrEmpty()) {
                return Result.Error(Exception("Refresh token is empty"))
            }
            val response = authApi.refresh("Bearer $refreshToken")
            val authTokens = response.toDomain()
            saveTokens(authTokens)
            Result.Success(authTokens)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            clearTokens()
            Result.Success(Unit)
        } catch (e: Exception) {
            // Даже при ошибке очищаем локальные токены
            clearTokens()
            Result.Error(e)
        }
    }

    override fun getAccessToken(): Flow<String?> {
        return tokenDataStore.getAccessToken()
    }

    override fun getRefreshToken(): Flow<String?> {
        return tokenDataStore.getRefreshToken()
    }

    override suspend fun saveTokens(authTokens: AuthTokens) {
        tokenDataStore.saveAuthData(
            accessToken = authTokens.accessToken,
            refreshToken = authTokens.refreshToken,
            accessTokenExpiresAt = authTokens.accessTokenExpiresAt.format(DateTimeFormatter.ISO_DATE_TIME),
            refreshTokenExpiresAt = authTokens.refreshTokenExpiresAt.format(DateTimeFormatter.ISO_DATE_TIME),
            userId = authTokens.userId.toString(),
            username = authTokens.username,
            email = authTokens.email
        )
    }

    override suspend fun clearTokens() {
        tokenDataStore.clear()
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return tokenDataStore.getAccessToken().map { !it.isNullOrEmpty() }
    }
}

