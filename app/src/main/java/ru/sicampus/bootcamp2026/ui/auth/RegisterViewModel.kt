package ru.sicampus.bootcamp2026.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * ViewModel для экрана регистрации
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = event.username) }
            }
            is RegisterUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is RegisterUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is RegisterUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.password) }
            }
            is RegisterUiEvent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is RegisterUiEvent.ToggleConfirmPasswordVisibility -> {
                _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }
            is RegisterUiEvent.Register -> register()
            is RegisterUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun register() {
        val state = _uiState.value

        // Валидация
        var hasErrors = false

        if (state.username.isBlank()) {
            _uiState.update { it.copy(error = "Username is required") }
            hasErrors = true
        } else if (state.username.length < 3) {
            _uiState.update { it.copy(error = "Username must be at least 3 characters") }
            hasErrors = true
        }

        if (state.email.isBlank()) {
            _uiState.update { it.copy(error = "Email is required") }
            hasErrors = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(error = "Invalid email format") }
            hasErrors = true
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(error = "Password is required") }
            hasErrors = true
        } else if (state.password.length < 6) {
            _uiState.update { it.copy(error = "Password must be at least 6 characters") }
            hasErrors = true
        }

        if (state.confirmPassword.isBlank()) {
            _uiState.update { it.copy(error = "Please confirm your password") }
            hasErrors = true
        } else if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            hasErrors = true
        }

        if (hasErrors) return

        // Регистрация
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.register(
                username = state.username,
                email = state.email,
                password = state.password
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            isSuccess = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Registration failed: ${result.exception.message}"
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }
}

