package ru.sicampus.bootcamp2026.ui.screen.auth.register

sealed interface RegisterState {
    data object Loading : RegisterState
    data class Data(
        val isEnabledSend: Boolean = false,
        val error: String? = null,
        val fieldErrors: Map<String, String> = emptyMap(),
        val visitedFields: Set<String> = emptySet()
    ) : RegisterState
    data object Success : RegisterState
}
data class RegisterFields(
    val name: String = "",
    val position: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = ""
)