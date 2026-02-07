package ru.sicampus.bootcamp2026.ui.screens.auth.login

import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.usecase.auth.AuthorizeUseCase


class LoginViewModel(private val authUseCase: AuthorizeUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvents: Channel<ActionState> = Channel()
    val navigationEvents: Flow<ActionState> = _navigationEvents.receiveAsFlow()

    fun login() {
        if (!validateInput()) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authUseCase(
                email = _uiState.value.email,
                password = _uiState.value.password
            )

            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    Log.d("test", "(LoginViewModel) Login error: ${error.message}", error)
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isLoginSuccess = false,
                            errorMessage = error.message ?: "Ошибка сервера"
                        )
                    }
                }
            )
        }
    }

    fun resetLoginState() {
        _uiState.update { it.copy(isLoginSuccess = false) }
    }

    fun navigate(actionState: ActionState) {
        viewModelScope.launch {
            _navigationEvents.send(actionState)
        }
    }

    private fun validateInput(): Boolean {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите email") }
            return false
        }

        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Некорректный email") }
            return false
        }

        if (password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите пароль") }
            return false
        }

        if ((password.length < 8) or (password.length > 64)) {
            _uiState.update { it.copy(errorMessage = "Пароль от 8 до 64 символов") }
            return false
        }

        return true
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }
}