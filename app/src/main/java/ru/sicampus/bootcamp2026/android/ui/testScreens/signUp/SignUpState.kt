package ru.sicampus.bootcamp2026.android.ui.testScreens.signUp

sealed interface SignUpState {
    data object Loading : SignUpState
    data class Data(
        val isEnabledSend: Boolean,
        val error: String?,
        val validationHint: String? = null
    ) : SignUpState
}