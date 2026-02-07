package ru.sicampus.bootcamp2026.data.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("name") val name: String,
    @SerializedName("position") val position: String,
    @SerializedName("id") val id: Long
)

data class RegisterRequest(
    @SerializedName("name") val fullName: String,
    @SerializedName("login") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("position") val position: String
)

data class LoginRequest(
    @SerializedName("login") val email: String,
    @SerializedName("password") val password: String
)

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)

data class ResetPasswordRequest(
    @SerializedName("token") val token: String,
    @SerializedName("newPassword") val newPassword: String
)