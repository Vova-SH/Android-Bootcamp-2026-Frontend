package ru.sicampus.bootcamp2026.screens.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.AuthRepository

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        login: String,
        password: String,
        confirmPassword: String,
        name: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ) {
        if (password != confirmPassword) {
            _uiState.value = RegisterUiState.Error("Пароли не совпадают")
            return
        }

        if (login.isBlank() || password.isBlank() || name.isBlank() || lastName.isBlank() || email.isBlank()) {
            _uiState.value = RegisterUiState.Error("Заполните все обязательные поля")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                authRepository.register(
                    login = login,
                    password = password,
                    name = name,
                    lastName = lastName,
                    email = email,
                    phoneNumber = phoneNumber
                )
                    .onSuccess {
                        _uiState.value = RegisterUiState.Success
                    }
                    .onFailure { error ->
                        _uiState.value = RegisterUiState.Error(error.message ?: "Ошибка регистрации")
                    }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error("Сетевая ошибка: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}