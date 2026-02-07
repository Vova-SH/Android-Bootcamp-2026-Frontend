package ru.sicampus.bootcamp2026.ui.screen.registration

sealed class RegistrationIntent {
    data class TextInput(
        val name: String,
        val login: String,
        val email: String,
        val password: String
    ) : RegistrationIntent()
    
    data class Register(
        val name: String,
        val login: String,
        val email: String,
        val password: String
    ) : RegistrationIntent()
}
