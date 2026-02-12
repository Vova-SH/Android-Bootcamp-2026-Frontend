package ru.sicampus.bootcamp2026.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sicampus.bootcamp2026.data.dto.EmployeeRequestDTO
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.auth.AuthState

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun checkAndAuth(
        login: String,
        password: String
    ): Result<Boolean>{
        AuthLocalDataSource.setToken(login, password)
        return authNetworkDataSource.login().onSuccess { isLogin ->
            if(!isLogin) authLocalDataSource.clearToken()

        }.onFailure {
            authLocalDataSource.clearToken()
        }

    }
    suspend fun register(employeeRequestDTO: EmployeeRequestDTO) : Result<Unit>{
        return authNetworkDataSource.register(employeeRequestDTO).onSuccess {
            authLocalDataSource.setToken(employeeRequestDTO.username, employeeRequestDTO.password)
        }.onFailure {
            authLocalDataSource.clearToken()
        }
    }
    suspend fun logout() {
        authLocalDataSource.clearToken()
    }
    val authState: Flow<AuthState> = authLocalDataSource.getToken()
        .map { token ->
            if (token.isNullOrBlank()) AuthState.Unauthorized else AuthState.Authorized
        }
}