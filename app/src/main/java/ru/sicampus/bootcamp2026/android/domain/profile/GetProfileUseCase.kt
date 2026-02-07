package ru.sicampus.bootcamp2026.android.domain.profile

import ru.sicampus.bootcamp2026.android.data.ProfileRepository
import ru.sicampus.bootcamp2026.android.data.source.PersonResponse

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<PersonResponse> {
        return profileRepository.getProfile()
    }
}