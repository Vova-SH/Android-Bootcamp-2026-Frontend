package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto

interface AuthRepository {
    suspend fun registerUser(user: UserRegisterDto): Result<UserDto>
    suspend fun loginUser(email: String, pass: String): Result<UserDto>
}