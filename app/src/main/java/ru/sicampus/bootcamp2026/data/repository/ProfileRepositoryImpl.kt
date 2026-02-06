package ru.sicampus.bootcamp2026.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.sicampus.bootcamp2026.data.mapper.toDomain
import ru.sicampus.bootcamp2026.data.remote.api.ProfileApi
import ru.sicampus.bootcamp2026.data.remote.dto.ResetPasswordRequest
import ru.sicampus.bootcamp2026.data.remote.dto.UpdateAvatarRequest
import ru.sicampus.bootcamp2026.data.remote.dto.UserProfileRequest
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.model.User
import ru.sicampus.bootcamp2026.domain.repository.ProfileRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация репозитория для работы с профилем
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi
) : ProfileRepository {

    private val _cachedProfile = MutableStateFlow<User?>(null)

    override suspend fun getProfile(): Result<User> {
        return try {
            val response = profileApi.getProfile()
            val user = response.toDomain()
            _cachedProfile.value = user
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateProfile(username: String?, avatarUrl: String?): Result<User> {
        return try {
            val response = profileApi.updateProfile(
                UserProfileRequest(
                    username = username,
                    avatarUrl = avatarUrl
                )
            )
            val user = response.toDomain()
            _cachedProfile.value = user
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateAvatar(avatarUrl: String): Result<User> {
        return try {
            val response = profileApi.updateAvatar(
                UpdateAvatarRequest(avatarUrl = avatarUrl)
            )
            val user = response.toDomain()
            _cachedProfile.value = user
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit> {
        return try {
            profileApi.resetPassword(
                ResetPasswordRequest(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAllUsers(page: Int, size: Int): Result<PaginatedData<User>> {
        return try {
            val response = profileApi.getAllUsers(page = page, size = size)
            val users = response.toDomain()
            Result.Success(users)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getCachedProfile(): Flow<User?> {
        return _cachedProfile
    }
}

