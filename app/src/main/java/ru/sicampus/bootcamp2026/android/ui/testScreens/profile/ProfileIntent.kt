package ru.sicampus.bootcamp2026.android.ui.testScreens.profile

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent
    data object ToggleEditMode : ProfileIntent
    data class UpdateProfile(
        val fullName: String,
        val department: String,
        val position: String
    ) : ProfileIntent
    data object Logout : ProfileIntent
}