package ru.sicampus.bootcamp2026.android.domain.profile

import ru.sicampus.bootcamp2026.android.data.ProfileRepository

class LogoutUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke() {
        profileRepository.logout()
    }
}