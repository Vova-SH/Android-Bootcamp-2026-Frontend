package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.repository.UserRepository

class DeleteAccountUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.deleteAccount()
    }
}