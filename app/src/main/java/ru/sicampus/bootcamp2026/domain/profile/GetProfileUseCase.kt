package ru.sicampus.bootcamp2026.domain.profile

import ru.sicampus.bootcamp2026.data.ProfileRepository
import ru.sicampus.bootcamp2026.domain.entities.EmployeeEntity

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<EmployeeEntity>{
        return profileRepository.getProfile()
    }
}