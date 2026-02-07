package ru.sicampus.bootcamp2026.domain.add

import androidx.compose.ui.geometry.Offset
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.dto.PagingUserListDto
import ru.sicampus.bootcamp2026.domain.add.entities.PagingUserListEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

class GetUsersUseCase( private val userRepository: UserRepository) {
    suspend operator fun invoke(
        offset: Int,
    ): Result<PagingUserListEntity>{
        return userRepository.getUsers(
            page = offset / COUNT,
            size = COUNT
        )
    }

    private companion object{
        const val COUNT = 20 //TODO количество элементов помещающихся в экран * 2
    }

}
