package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.dto.RegistrationRequestDto
import ru.sicampus.bootcamp2026.data.source.AuthApiService
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource


class AuthRepository(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authApiService: AuthApiService = AuthApiService()
) {
    suspend fun checkAndAuth(login: String, password: String): Boolean {
        authLocalDataSource.setToken(login, password)
        val result = authApiService.checkAuth()
        if (!result) authLocalDataSource.clearToken()
        return result
    }
    suspend fun register(
        login: String,
        password: String,
        name: String,
        email: String?
    ): Result<Unit> {
        return try {
            val response = authApiService.register(
                RegistrationRequestDto(
                    email = login,
                    password = password,
                    fullName = name
                )
            )
            authLocalDataSource.setToken(login, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
