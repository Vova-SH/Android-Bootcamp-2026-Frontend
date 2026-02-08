package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.repository.UserRepository

class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<List<UserDto>> {
        return repository.getAllUsers()
    }
}