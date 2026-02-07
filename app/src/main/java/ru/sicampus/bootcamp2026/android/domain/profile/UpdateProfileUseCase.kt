package ru.sicampus.bootcamp2026.android.domain.profile

import ru.sicampus.bootcamp2026.android.data.ProfileRepository
import ru.sicampus.bootcamp2026.android.data.source.PersonResponse

class UpdateProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(
        fullName: String,
        department: String,
        position: String
    ): Result<PersonResponse> {
        return profileRepository.updateProfile(
            fullName = fullName,
            department = department,
            position = position
        )
    }
}