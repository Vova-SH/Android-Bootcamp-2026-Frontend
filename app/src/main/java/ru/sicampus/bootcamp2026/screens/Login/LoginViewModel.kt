package ru.sicampus.bootcamp2026.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.domain.auth.CheckAuthFormatUseCase

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val checkAuthFormatUseCase: CheckAuthFormatUseCase = CheckAuthFormatUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(login: String, password: String) {
        if (!checkAuthFormatUseCase(login, password)) {
            _uiState.value = LoginUiState.Error("Неверный логин или пароль")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                authRepository.login(login, password)
                    .onSuccess {
                        _uiState.value = LoginUiState.Success
                    }
                    .onFailure { error ->
                        _uiState.value = LoginUiState.Error(error.message ?: "Ошибка входа")
                    }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Сетевая ошибка: ${e.message}")
            }
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}