package ru.sicampus.bootcamp2026.domain.auth

import kotlinx.coroutines.flow.Flow
import ru.sicampus.bootcamp2026.data.repository.AuthRepository

class GetAuthStatusUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthState> = authRepository.authState
}