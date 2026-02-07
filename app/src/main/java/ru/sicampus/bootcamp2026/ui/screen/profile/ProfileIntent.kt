package ru.sicampus.bootcamp2026.ui.screen.profile

import ru.sicampus.bootcamp2026.domain.entities.ProfileUpdateEntity

sealed interface ProfileIntent {
    data object Load : ProfileIntent
    data object Cancel : ProfileIntent
    data class Save(val updatedEmployee: ProfileUpdateEntity) : ProfileIntent
    data object Logout : ProfileIntent
    data object SetEditMode : ProfileIntent
}