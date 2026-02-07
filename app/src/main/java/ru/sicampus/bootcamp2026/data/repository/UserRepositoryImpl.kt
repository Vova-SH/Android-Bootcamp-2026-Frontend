package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.SessionManager
import ru.sicampus.bootcamp2026.data.source.UserService
import ru.sicampus.bootcamp2026.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val service = UserService()

    override suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            val userId = SessionManager.currentUserId ?: throw IllegalStateException("Session expired")
            val user = service.getUserById(userId)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCurrentUser(
        firstName: String,
        secondName: String,
        position: String?
    ): Result<UserDto> {
        return try {
            val userId = SessionManager.currentUserId ?: throw IllegalStateException("Session expired")
            val currentUser = service.getUserById(userId)

            val updatedDto = currentUser.copy(
                firstName = firstName,
                secondName = secondName,
                position = position
            )

            val result = service.updateUser(userId, updatedDto)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}