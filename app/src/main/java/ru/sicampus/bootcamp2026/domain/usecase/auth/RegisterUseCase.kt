package ru.sicampus.bootcamp2026.domain.usecase.auth

import ru.sicampus.bootcamp2026.domain.model.AuthTokens
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * Use case для регистрации пользователя
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Result<AuthTokens> {
        // Валидация
        if (username.isBlank() || username.length < 2) {
            return Result.Error(Exception("Имя пользователя должно быть не менее 2 символов"))
        }
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error(Exception("Некорректный email"))
        }
        if (password.length < 6) {
            return Result.Error(Exception("Пароль должен быть не менее 6 символов"))
        }

        return authRepository.register(username, email, password)
    }
}

