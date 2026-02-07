package ru.sicampus.bootcamp2026.domain.auth

import android.provider.ContactsContract
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.UserPreferences


class CheckAndSaveAuthUseCase(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke(
        login: String,
        password: String
    ): Result<Boolean> {
        return runCatching {
            userPreferences.saveUserEmail(login)
            val isLogin = authRepository.checkAndAuth(login, password)
            if (!isLogin) {
                throw Exception("Login or pass incorrect")
            }
            isLogin
        }
    }
}

