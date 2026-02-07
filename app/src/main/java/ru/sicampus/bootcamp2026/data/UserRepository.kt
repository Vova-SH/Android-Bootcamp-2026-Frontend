package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.mappers.toEntity
import ru.sicampus.bootcamp2026.data.source.AuthApiService
import ru.sicampus.bootcamp2026.data.source.UserApiService
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class UserRepository(
    private val apiService: UserApiService = UserApiService(),
    private val authApiService: AuthApiService = AuthApiService()
){
    suspend fun getUsers(page: Int = 0, size: Int = 20, search: String? = null): Result<List<UserEntity>>{
        return try {
            val users = apiService.getUsers(page, size, search)
            Result.success(users.map { it.toEntity() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<UserEntity> {
        return try {
            val userDto = authApiService.getCurrentUser()
            Result.success(userDto.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(fullName: String): Result<UserEntity> {
        return try {
            val userDto = authApiService.updateUser(fullName)
            Result.success(userDto.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}