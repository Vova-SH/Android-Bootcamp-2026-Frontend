package ru.sicampus.bootcamp2026.data.auth

class AuthRepository(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) {
    suspend fun register(fullName: String, email: String, password: String): Result<Unit> =
        runCatching {
            val res = api.register(RegisterRequest(email.trim(), password, fullName.trim()))
            tokenStorage.saveToken(res.token)
        }

    suspend fun login(email: String, password: String): Result<Unit> =
        runCatching {
            val res = api.login(LoginRequest(email.trim(), password))
            tokenStorage.saveToken(res.token)
        }


    fun logout() {
        tokenStorage.clearToken()
    }
}
