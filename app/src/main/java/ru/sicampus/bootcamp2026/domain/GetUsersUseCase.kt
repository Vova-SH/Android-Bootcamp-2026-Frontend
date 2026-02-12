package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.PagingUserListEntity
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        offset: Int
    ): Result<PagingUserListEntity> {
        return userRepository.getUsers(
            page = offset / COUNT,
            size = COUNT,
        )
    }

    private companion object {
        const val COUNT = 2
    }
}