package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.PagingUserListEntity

class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        offset: Int
    ): Result<PagingUserListEntity> {
        return userRepository.getUsers(
            page = offset / COUNT,
            count = COUNT
        )
    }

    private companion object {
        const val COUNT = 20
    }
}

