package ru.sicampus.bootcamp2026.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.remote.TokenRefreshService
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.repository.ProfileRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * ViewModel для экрана профиля
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val tokenRefreshService: TokenRefreshService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.LoadProfile -> loadProfile()
            is ProfileUiEvent.EnableEditMode -> enableEditMode()
            is ProfileUiEvent.CancelEdit -> cancelEdit()
            is ProfileUiEvent.UsernameChanged -> {
                _uiState.update { it.copy(editUsername = event.username, usernameError = null) }
            }
            is ProfileUiEvent.AvatarUrlChanged -> {
                _uiState.update { it.copy(editAvatarUrl = event.url) }
            }
            is ProfileUiEvent.SaveProfile -> saveProfile()
            is ProfileUiEvent.OpenPasswordChange -> {
                _uiState.update { it.copy(isChangingPassword = true) }
            }
            is ProfileUiEvent.CancelPasswordChange -> {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        currentPassword = "",
                        newPassword = "",
                        confirmPassword = "",
                        currentPasswordError = null,
                        newPasswordError = null,
                        confirmPasswordError = null
                    )
                }
            }
            is ProfileUiEvent.CurrentPasswordChanged -> {
                _uiState.update { it.copy(currentPassword = event.password, currentPasswordError = null) }
            }
            is ProfileUiEvent.NewPasswordChanged -> {
                _uiState.update { it.copy(newPassword = event.password, newPasswordError = null) }
            }
            is ProfileUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.password, confirmPasswordError = null) }
            }
            is ProfileUiEvent.ChangePassword -> changePassword()
            is ProfileUiEvent.Logout -> logout()
            is ProfileUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
            is ProfileUiEvent.DismissSuccess -> {
                _uiState.update { it.copy(successMessage = null) }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = profileRepository.getProfile()
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            userId = result.data.id,
                            username = result.data.username,
                            email = result.data.email,
                            avatarUrl = result.data.avatarUrl,
                            editUsername = result.data.username,
                            editAvatarUrl = result.data.avatarUrl ?: "",
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to load profile: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    private fun enableEditMode() {
        val state = _uiState.value
        _uiState.update {
            it.copy(
                isEditMode = true,
                editUsername = state.username,
                editAvatarUrl = state.avatarUrl ?: ""
            )
        }
    }

    private fun cancelEdit() {
        _uiState.update {
            it.copy(
                isEditMode = false,
                usernameError = null
            )
        }
    }

    private fun saveProfile() {
        val state = _uiState.value

        // Валидация
        if (state.editUsername.isBlank()) {
            _uiState.update { it.copy(usernameError = "Username cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = profileRepository.updateProfile(
                username = state.editUsername,
                avatarUrl = state.editAvatarUrl.takeIf { it.isNotBlank() }
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            username = result.data.username,
                            avatarUrl = result.data.avatarUrl,
                            isEditMode = false,
                            isLoading = false,
                            successMessage = "Profile updated successfully"
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to update profile: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    private fun changePassword() {
        val state = _uiState.value

        // Валидация
        var hasErrors = false

        if (state.currentPassword.isBlank()) {
            _uiState.update { it.copy(currentPasswordError = "Current password is required") }
            hasErrors = true
        }

        if (state.newPassword.length < 6) {
            _uiState.update { it.copy(newPasswordError = "Password must be at least 6 characters") }
            hasErrors = true
        }

        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            hasErrors = true
        }

        if (hasErrors) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = profileRepository.resetPassword(
                currentPassword = state.currentPassword,
                newPassword = state.newPassword,
                confirmPassword = state.confirmPassword
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isChangingPassword = false,
                            currentPassword = "",
                            newPassword = "",
                            confirmPassword = "",
                            isLoading = false,
                            successMessage = "Password changed successfully"
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to change password: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.logout()
            when (result) {
                is Result.Success -> {
                    // Остановить менеджер обновления токенов
                    tokenRefreshService.onLogout()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            shouldNavigateToLogin = true
                        )
                    }
                }
                is Result.Error -> {
                    // Даже при ошибке переходим на экран логина и останавливаем менеджер
                    tokenRefreshService.onLogout()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            shouldNavigateToLogin = true
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }
}

