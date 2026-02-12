package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.PersonResponse
import ru.sicampus.bootcamp2026.android.data.source.ProfileNetworkDataSource

class ProfileRepository(
    private val profileNetworkDataSource: ProfileNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun getProfile(): Result<PersonResponse> {
        val userId = authLocalDataSource.getUserId() 
            ?: return Result.failure(Exception("User ID not found"))
        return profileNetworkDataSource.getProfile(userId)
    }

    suspend fun updateProfile(
        fullName: String,
        department: String,
        position: String
    ): Result<PersonResponse> {
        val userId = authLocalDataSource.getUserId()
            ?: return Result.failure(Exception("User ID not found"))
        return profileNetworkDataSource.updateProfile(
            userId = userId,
            fullName = fullName,
            department = department,
            position = position
        )
    }

    suspend fun logout() {
        authLocalDataSource.clearAll()
    }
}