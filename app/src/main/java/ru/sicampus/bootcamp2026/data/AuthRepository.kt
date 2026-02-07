package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.dto.user.UserRegisterDTO
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource

class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun checkAndAuth(
        login: String,
        password: String
    ): Boolean {
        authLocalDataSource.setToken(login, password)
        val result = authNetworkDataSource.checkAuth(
            authLocalDataSource.token ?: return false
        )
        if (!result) authLocalDataSource.clearToken()
        return result
    }


    suspend fun register(request: RegisterResponse): Result<Boolean> {
        return runCatching {
            val dto = UserRegisterDTO(
                email = request.email,
                password = request.password,
                fullName = request.fullName,
                jobTitle = request.jobTitle,
                passwordConfirm = request.passwordConfirm,
                department = request.department
            )
            authNetworkDataSource.registration(dto)

        }
    }

}