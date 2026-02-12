package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.network.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class SearchUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend fun invoke(search: String): Result<List<UserEntity>> {
        return userRepository.searchUsers(search)
    }
}