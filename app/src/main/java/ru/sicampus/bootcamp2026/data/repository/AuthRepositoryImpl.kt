package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.data.source.AuthService
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    private val service = AuthService()

    override suspend fun registerUser(user: UserRegisterDto): Result<UserDto> {
        return try {
            val response = service.register(user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(email: String, pass: String): Result<UserDto> {
        return try {
            val response = service.login(email, pass)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}