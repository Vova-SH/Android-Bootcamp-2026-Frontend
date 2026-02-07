package ru.sicampus.bootcamp2026.android.ui.testScreens.signUp

sealed interface SignUpIntent {
    data class Send(
        val fullName: String,
        val department: String,
        val position: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ) : SignUpIntent

    data class TextInput(
        val fullName: String,
        val department: String,
        val position: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ) : SignUpIntent

    data object NavigateBack : SignUpIntent
}