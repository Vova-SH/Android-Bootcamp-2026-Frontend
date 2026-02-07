package ru.sicampus.bootcamp2026.domain


import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UserApiService
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class GetUsersUseCase (
    private val userRepository : UserRepository = UserRepository(
        UserApiService()
    )
) {
    suspend operator fun invoke(
        page: Int,
        query: String? = null
    ): Result<List<UserEntity>>{
        return userRepository.getUsers(
            page = page,
            size = COUNT,
            search = query
        )
    }

    private companion object {
        const val  COUNT = 5
    }
}