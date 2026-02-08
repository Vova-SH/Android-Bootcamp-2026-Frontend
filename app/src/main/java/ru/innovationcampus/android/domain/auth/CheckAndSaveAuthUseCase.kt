package ru.innovationcampus.android.domain.auth

import ru.innovationcampus.android.data.AuthRepository

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