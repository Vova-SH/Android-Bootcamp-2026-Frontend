package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.data.AuthRepository

class LogoutEmployeeUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(){
        return authRepository.logout()
    }
}