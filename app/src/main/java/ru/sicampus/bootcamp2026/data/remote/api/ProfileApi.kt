package ru.sicampus.bootcamp2026.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import ru.sicampus.bootcamp2026.data.remote.dto.PageResponse
import ru.sicampus.bootcamp2026.data.remote.dto.ResetPasswordRequest
import ru.sicampus.bootcamp2026.data.remote.dto.UpdateAvatarRequest
import ru.sicampus.bootcamp2026.data.remote.dto.UserProfileRequest
import ru.sicampus.bootcamp2026.data.remote.dto.UserProfileResponse

/**
 * API интерфейс для работы с профилем
 */
interface ProfileApi {

    @GET("api/v1/profile")
    suspend fun getProfile(): UserProfileResponse

    @PUT("api/v1/profile")
    suspend fun updateProfile(
        @Body request: UserProfileRequest
    ): UserProfileResponse

    @PUT("api/v1/profile/avatar")
    suspend fun updateAvatar(
        @Body request: UpdateAvatarRequest
    ): UserProfileResponse

    @PUT("api/v1/profile/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    )

    @GET("api/v1/profile/public/all")
    suspend fun getAllUsers(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PageResponse<UserProfileResponse>
}

