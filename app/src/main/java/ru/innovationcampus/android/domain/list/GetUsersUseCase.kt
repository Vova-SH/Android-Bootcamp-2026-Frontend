package ru.innovationcampus.android.domain.list

import ru.innovationcampus.android.data.UserRepository
import ru.innovationcampus.android.domain.list.entities.PagingUserListEntity

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
        const val COUNT = 20
    }
}