package ru.sicampus.bootcamp2026.domain.usecase.auth

import ru.sicampus.bootcamp2026.domain.model.AuthTokens
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * Use case для входа пользователя
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<AuthTokens> {
        // Валидация
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error(Exception("Некорректный email"))
        }
        if (password.isBlank()) {
            return Result.Error(Exception("Пароль не может быть пустым"))
        }

        return authRepository.login(email, password)
    }
}

