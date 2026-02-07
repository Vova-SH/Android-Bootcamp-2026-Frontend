package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource


class CheckAndSaveAuthUseCase(
    private val authRepository: AuthRepository = AuthRepository(
        AuthLocalDataSource
    )
) {
    suspend operator fun invoke(
        login: String,
        password: String,
    ): Result<Unit> {
        return try {
            val isSuccess = authRepository.checkAndAuth(login, password)
            if (isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Неверный логин или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
