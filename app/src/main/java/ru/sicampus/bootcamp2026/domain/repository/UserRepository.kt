package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.data.dto.UserDto

interface UserRepository {
    suspend fun getUserProfile(): Result<UserDto>
    suspend fun updateUserProfile(firstName: String, secondName: String, position: String?): Result<UserDto>
    suspend fun getAllUsers(): Result<List<UserDto>>
    suspend fun getUsersPaginated(page: Int, size: Int): Result<List<UserDto>>
    suspend fun getUserByEmail(email: String): Result<UserDto>
    suspend fun getUserById(id: Long): Result<UserDto>
    suspend fun deleteAccount(): Result<Unit>
}