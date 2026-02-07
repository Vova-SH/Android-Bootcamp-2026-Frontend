package ru.sicampus.bootcamp2026.android.domain.auth

import ru.sicampus.bootcamp2026.android.data.AuthRepository

class CheckAndSaveAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String,
    ): Result<Unit> {
        return authRepository.checkAndAuth(login, password).mapCatching { isLogin ->
            if (!isLogin) error("Login or pass incorrect")
        }
    }
}