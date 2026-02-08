package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.TokenStorage
import ru.sicampus.bootcamp2026.data.source.UserService
import ru.sicampus.bootcamp2026.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val service = UserService()

    override suspend fun getUserProfile(): Result<UserDto> {
        return try {
            val userId = TokenStorage.userId
                ?: throw IllegalStateException("Пользователь не авторизован (ID не найден)")
            val user = service.getUserById(userId)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(
        firstName: String,
        secondName: String,
        position: String?
    ): Result<UserDto> {
        return try {
            val userId = TokenStorage.userId
                ?: throw IllegalStateException("ID не найден")
            val currentUser = service.getUserById(userId)
            val updatedUser = currentUser.copy(
                firstName = firstName,
                secondName = secondName,
                position = position
            )
            val result = service.updateUser(userId, updatedUser)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllUsers(): Result<List<UserDto>> {
        return try {
            val users = service.getAllUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsersPaginated(page: Int, size: Int): Result<List<UserDto>> {
        return try {
            val response = service.getUsersPaginated(page, size)
            Result.success(response.content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Result<UserDto> {
        return try {
            val user = service.getUserByEmail(email)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(id: Long): Result<UserDto> {
        return try {
            val user = service.getUserById(id)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val userId = TokenStorage.userId ?: throw IllegalStateException("User ID not found")
            service.deleteUser(userId)
            TokenStorage.clear()
            ru.sicampus.bootcamp2026.data.source.SessionManager.clear()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}