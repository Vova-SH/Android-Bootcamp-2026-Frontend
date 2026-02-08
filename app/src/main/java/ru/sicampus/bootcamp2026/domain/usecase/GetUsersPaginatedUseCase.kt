package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.repository.UserRepository

class GetUsersPaginatedUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(page: Int, size: Int): Result<List<UserDto>> {
        return repository.getUsersPaginated(page, size)
    }
}