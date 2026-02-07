package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.repository.UserRepository

class UpdateUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(firstName: String, secondName: String, position: String?): Result<UserDto> {
        return repository.updateCurrentUser(firstName, secondName, position)
    }
}