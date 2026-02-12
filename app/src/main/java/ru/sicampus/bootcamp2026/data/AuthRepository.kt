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
            val registrationEmail = if (email.isNullOrBlank()) login else email
            authApiService.register(
                RegistrationRequestDto(
                    email = registrationEmail,
                    password = password,
                    fullName = name
                )
            )
            // Важно: Сначала устанавливаем токен, чтобы Network.resetClient() сработал
            authLocalDataSource.setToken(registrationEmail, password)
            
            // Проверяем авторизацию, чтобы убедиться, что всё ок
            val isAuthOk = authApiService.checkAuth()
            if (isAuthOk) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка авторизации после регистрации"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
