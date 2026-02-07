package ru.sicampus.bootcamp2026.ui.profile

import java.util.UUID

/**
 * UI состояние для экрана профиля
 */
data class ProfileUiState(
    // Данные пользователя
    val userId: UUID? = null,
    val username: String = "",
    val email: String = "",
    val avatarUrl: String? = null,

    // Режимы экрана
    val isEditMode: Boolean = false,
    val isChangingPassword: Boolean = false,

    // Поля для редактирования
    val editUsername: String = "",
    val editAvatarUrl: String = "",

    // Поля для смены пароля
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",

    // Ошибки валидации
    val usernameError: String? = null,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,

    // Состояние загрузки
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    // Навигация
    val shouldNavigateToLogin: Boolean = false
)

/**
 * События UI для экрана профиля
 */
sealed interface ProfileUiEvent {
    // Загрузка профиля
    data object LoadProfile : ProfileUiEvent

    // Режим редактирования
    data object EnableEditMode : ProfileUiEvent
    data object CancelEdit : ProfileUiEvent
    data class UsernameChanged(val username: String) : ProfileUiEvent
    data class AvatarUrlChanged(val url: String) : ProfileUiEvent
    data object SaveProfile : ProfileUiEvent

    // Смена пароля
    data object OpenPasswordChange : ProfileUiEvent
    data object CancelPasswordChange : ProfileUiEvent
    data class CurrentPasswordChanged(val password: String) : ProfileUiEvent
    data class NewPasswordChanged(val password: String) : ProfileUiEvent
    data class ConfirmPasswordChanged(val password: String) : ProfileUiEvent
    data object ChangePassword : ProfileUiEvent

    // Выход
    data object Logout : ProfileUiEvent

    // Очистка сообщений
    data object DismissError : ProfileUiEvent
    data object DismissSuccess : ProfileUiEvent
}

