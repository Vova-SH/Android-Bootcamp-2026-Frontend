package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.data.source.AuthService
import ru.sicampus.bootcamp2026.data.source.SessionManager
import ru.sicampus.bootcamp2026.data.source.TokenStorage
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    private val service = AuthService()

    override suspend fun registerUser(user: UserRegisterDto): Result<UserDto> {
        return try {
            val result = service.register(user)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(email: String, pass: String): Result<UserDto> {
        return try {
            val user = service.login(email, pass)

            val token = SessionManager.authHeader
                ?: throw IllegalStateException("Token missing after login")

            TokenStorage.accessToken = token
            TokenStorage.userId = user.id

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        TokenStorage.clear()
        SessionManager.clear()
    }

    override fun isUserLoggedIn(): Boolean {
        return SessionManager.isLoggedIn()
    }
}