package ru.sicampus.bootcamp2026.domain.usecase.profile

import ru.sicampus.bootcamp2026.domain.model.User
import ru.sicampus.bootcamp2026.domain.repository.ProfileRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * Use case для получения профиля пользователя
 */
class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<User> {
        return profileRepository.getProfile()
    }
}

