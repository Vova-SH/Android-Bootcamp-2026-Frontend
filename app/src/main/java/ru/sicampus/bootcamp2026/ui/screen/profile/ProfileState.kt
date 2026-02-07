package ru.sicampus.bootcamp2026.ui.screen.profile

sealed interface ProfileState {
    data class Error(val reason: String) : ProfileState
    data object Loading : ProfileState
    data class Content(
        val fullName: String,
        val email: String,
        val pfpUrl: String? = null
    ) : ProfileState
}
