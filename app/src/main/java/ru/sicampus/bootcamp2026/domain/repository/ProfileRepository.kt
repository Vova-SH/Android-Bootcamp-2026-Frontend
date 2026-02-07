package ru.sicampus.bootcamp2026.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.model.User
import ru.sicampus.bootcamp2026.domain.util.Result

/**
 * Репозиторий для работы с профилем пользователя
 */
interface ProfileRepository {

    /**
     * Получение профиля текущего пользователя
     */
    suspend fun getProfile(): Result<User>

    /**
     * Обновление профиля
     */
    suspend fun updateProfile(
        username: String?,
        avatarUrl: String?
    ): Result<User>

    /**
     * Обновление аватара
     */
    suspend fun updateAvatar(avatarUrl: String): Result<User>

    /**
     * Сброс пароля
     */
    suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit>

    /**
     * Получение списка всех пользователей (публичный эндпоинт)
     */
    suspend fun getAllUsers(
        page: Int = 0,
        size: Int = 20
    ): Result<PaginatedData<User>>

    /**
     * Получение кэшированного профиля
     */
    fun getCachedProfile(): Flow<User?>
}

