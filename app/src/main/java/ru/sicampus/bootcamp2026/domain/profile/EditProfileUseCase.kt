package ru.sicampus.bootcamp2026.domain.profile

import ru.sicampus.bootcamp2026.data.ProfileRepository
import ru.sicampus.bootcamp2026.domain.entities.ProfileUpdateEntity

class EditProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(employeeEntity: ProfileUpdateEntity): Result<Unit>{
        return profileRepository.editProfile(employeeEntity)
    }
}