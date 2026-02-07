package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.data.AuthRepository

class CheckAndSaveLoginUseCase(
    private val authRepository: AuthRepository,

    ) {
    suspend operator fun invoke(
        login: String,
        password: String,
    ): Result<Unit> {
        return authRepository.checkAndAuth(login, password).mapCatching { isLogin ->
            if(!isLogin) error("Login or password incorrect")

        }
    }
}