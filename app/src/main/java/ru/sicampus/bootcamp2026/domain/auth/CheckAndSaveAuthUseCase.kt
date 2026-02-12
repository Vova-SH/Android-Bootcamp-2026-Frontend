package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.data.network.AuthRepository

class CheckAndSaveAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.checkAndAuth(email, password)
            .map {
                Unit
            }
    }
}