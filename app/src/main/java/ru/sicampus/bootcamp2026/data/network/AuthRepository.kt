package ru.sicampus.bootcamp2026.data.network

import ru.sicampus.bootcamp2026.data.network.dto.EmployeesDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import kotlin.onSuccess

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun checkAndAuth(email: String, password: String): Result<UserEntity> {
        return authNetworkDataSource.login(email, password)
            .onSuccess { userData ->
                authLocalDataSource.setToken(userData.token)
            }
    }
}