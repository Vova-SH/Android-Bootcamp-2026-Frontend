package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.data.dto.UserDto

interface UserRepository {
    suspend fun getCurrentUser(): Result<UserDto>
    suspend fun updateCurrentUser(firstName: String, secondName: String, position: String?): Result<UserDto>
}