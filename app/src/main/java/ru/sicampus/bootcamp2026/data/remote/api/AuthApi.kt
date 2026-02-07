package ru.sicampus.bootcamp2026.data.remote.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.sicampus.bootcamp2026.data.remote.dto.AuthResponse
import ru.sicampus.bootcamp2026.data.remote.dto.LoginRequest
import ru.sicampus.bootcamp2026.data.remote.dto.RegisterRequest

/**
 * API интерфейс для аутентификации
 */
interface AuthApi {

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("api/v1/auth/refresh")
    suspend fun refresh(
        @Header("Authorization") refreshToken: String
    ): AuthResponse

    @POST("api/v1/auth/logout")
    suspend fun logout()
}

