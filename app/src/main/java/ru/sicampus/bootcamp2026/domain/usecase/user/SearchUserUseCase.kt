package ru.sicampus.bootcamp2026.domain.usecase.user

import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserMini

class SearchUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 0,
        size: Int = 10
    ): Result<List<UserMini>> {
        return userRepository.searchUsers(query, page, size)
    }
}