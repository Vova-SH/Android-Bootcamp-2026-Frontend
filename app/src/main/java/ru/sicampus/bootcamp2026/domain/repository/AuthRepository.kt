package ru.sicampus.bootcamp2026.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.sicampus.bootcamp2026.domain.model.AuthTokens
import ru.sicampus.bootcamp2026.domain.util.Result

/**
 * Репозиторий для работы с аутентификацией
 */
interface AuthRepository {

    /**
     * Регистрация нового пользователя
     */
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<AuthTokens>

    /**
     * Вход в систему
     */
    suspend fun login(
        email: String,
        password: String
    ): Result<AuthTokens>

    /**
     * Обновление токенов
     */
    suspend fun refresh(): Result<AuthTokens>

    /**
     * Выход из системы
     */
    suspend fun logout(): Result<Unit>

    /**
     * Получение текущего access токена
     */
    fun getAccessToken(): Flow<String?>

    /**
     * Получение текущего refresh токена
     */
    fun getRefreshToken(): Flow<String?>

    /**
     * Сохранение токенов
     */
    suspend fun saveTokens(authTokens: AuthTokens)

    /**
     * Очистка токенов
     */
    suspend fun clearTokens()

    /**
     * Проверка авторизации
     */
    fun isAuthenticated(): Flow<Boolean>
}

