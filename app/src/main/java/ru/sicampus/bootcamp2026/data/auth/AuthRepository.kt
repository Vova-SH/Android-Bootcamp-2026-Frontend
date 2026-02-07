package ru.sicampus.bootcamp2026.data.auth

import android.util.Log

class AuthRepository(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) {
    suspend fun register(name: String, email: String, pass: String, position: String): Result<String> =
        runCatching {
            val req = RegisterRequest(name, email, pass, position)
            api.register(req).string()
        }

    suspend fun login(email: String, pass: String): Result<Unit> =
        runCatching {
            val res = api.login(LoginRequest(email, pass))
            Log.d("AUTH", "Token received: ${res.token}")
            tokenStorage.saveToken(res.token)
            tokenStorage.saveUserId(res.id)
        }

    suspend fun confirmEmail(code: String): Result<String> =
        runCatching {
            api.confirm(code).string()
        }

    suspend fun forgotPassword(email: String): Result<String> =
        runCatching {
            api.forgotPassword(ForgotPasswordRequest(email)).string()
        }

    suspend fun resetPassword(token: String, newPass: String): Result<String> =
        runCatching {
            api.resetPassword(ResetPasswordRequest(token, newPass)).string()
        }

    fun logout() {
        tokenStorage.clearToken()
    }
}