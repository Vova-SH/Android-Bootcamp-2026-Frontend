package ru.sicampus.bootcamp2026.data.auth

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): ResponseBody

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ResponseBody

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResponseBody

    @POST("api/auth/confirm")
    suspend fun confirm(@Query("token") token: String): ResponseBody
}