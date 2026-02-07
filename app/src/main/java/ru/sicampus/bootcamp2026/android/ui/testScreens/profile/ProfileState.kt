package ru.sicampus.bootcamp2026.android.ui.testScreens.profile

sealed interface ProfileState {
    data object Loading : ProfileState
    data class Data(
        val email: String,
        val fullName: String,
        val department: String,
        val position: String,
        val isEditMode: Boolean = false,
        val error: String? = null
    ) : ProfileState
}