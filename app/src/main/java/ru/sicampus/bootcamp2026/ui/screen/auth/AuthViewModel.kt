package ru.sicampus.bootcamp2026.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.CheckAndSaveAuthUseCase
import ru.sicampus.bootcamp2026.domain.CheckAuthFormatUseCase

class AuthViewModel : ViewModel() {
    private val checkAuthFormatUseCase by lazy { CheckAuthFormatUseCase() }
    private val checkAndSaveAuthUseCase by lazy { CheckAndSaveAuthUseCase() }
    
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Send -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                    checkAndSaveAuthUseCase(intent.login.trim(), intent.password.trim()).fold(
                        onSuccess = {
                            _uiState.update { it.copy(isLoading = false, isAuthorized = true) }
                        },
                        onFailure = { error ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false, 
                                    error = error.message ?: "Ошибка авторизации"
                                ) 
                            }
                        }
                    )
                }
            }
            is AuthIntent.TextInput -> {
                _uiState.update { state ->
                    state.copy(
                        email = intent.login,
                        password = intent.password,
                        isEnabledSend = checkAuthFormatUseCase(
                            intent.login,
                            intent.password
                        ),
                        error = null
                    )
                }
            }
        }
    }
}
