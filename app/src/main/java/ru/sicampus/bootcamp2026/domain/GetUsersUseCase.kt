package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class GetUsersUseCase(
    private val UserRepository: UserRepository
) {
    suspend operator fun invoke(
        page: Int
    ): Result<List<UserEntity>> {
        return UserRepository.getUsers(
            page = page,
            size = COUNT,
        )
    }

    private companion object{
        const val COUNT = 5
    }
}