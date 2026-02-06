package ru.sicampus.bootcamp2026.domain.usecase.auth

import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * Use case для выхода из системы
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}

