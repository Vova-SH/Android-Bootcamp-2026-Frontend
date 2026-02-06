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
 * ViewModel для экрана входа
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, emailError = null) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, passwordError = null) }
            }
            is LoginUiEvent.Login -> login()
            is LoginUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun login() {
        val state = _uiState.value

        // Валидация
        var hasErrors = false

        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email is required") }
            hasErrors = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(emailError = "Invalid email format") }
            hasErrors = true
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasErrors = true
        } else if (state.password.length < 6) {
            _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
            hasErrors = true
        }

        if (hasErrors) return

        // Вход
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.login(
                email = state.email,
                password = state.password
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Login failed: ${result.exception.message}"
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }
}

