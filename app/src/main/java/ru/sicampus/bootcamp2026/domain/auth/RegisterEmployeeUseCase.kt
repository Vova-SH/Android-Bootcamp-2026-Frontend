package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.data.repository.AuthRepository
import ru.sicampus.bootcamp2026.data.dto.toDTO
import ru.sicampus.bootcamp2026.ui.screen.auth.register.RegisterFields

class RegisterEmployeeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(fields: RegisterFields): Result<Unit> {
        return repository.register(fields.toDTO())
    }
}