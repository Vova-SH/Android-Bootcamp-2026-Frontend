package ru.sicampus.bootcamp2026.domain.users

import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.domain.users.entities.PagingUserListEntity

class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        offset: Int
    ): Result<PagingUserListEntity> {
        return userRepository.getUsers(
            page = offset / COUNT,
            size = COUNT
        )
    }

    private companion object {
        const val COUNT = 20
    }
}