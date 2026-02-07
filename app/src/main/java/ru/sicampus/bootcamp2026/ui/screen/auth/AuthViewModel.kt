package ru.sicampus.bootcamp2026.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.domain.usecase.LoginUseCase
import ru.sicampus.bootcamp2026.domain.usecase.RegisterUseCase
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            loginUseCase(email, pass)
                .onSuccess { _uiState.value = AuthUiState.Success(it) }
                .onFailure { _uiState.value = AuthUiState.Error("Login failed: ${it.message}") }
        }
    }

    fun register(dto: UserRegisterDto) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            registerUseCase(dto)
                .onSuccess { _uiState.value = AuthUiState.Success(it) }
                .onFailure { _uiState.value = AuthUiState.Error("Registration failed: ${it.message}") }
        }
    }

    fun clearError() {
        _uiState.value = AuthUiState.Idle
    }
}