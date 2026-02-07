package ru.sicampus.bootcamp2026.ui.screen.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.RegisterUseCase

class RegistrationViewModel : ViewModel() {
    private val registerUseCase by lazy { RegisterUseCase() }
    
    private val _uiState = MutableStateFlow(RegistrationState())
    val uiState: StateFlow<RegistrationState> = _uiState.asStateFlow()

    fun onIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.TextInput -> {
                _uiState.update { state ->
                    val trimmedEmail = intent.email.trim()
                    val trimmedName = intent.name.trim()
                    val trimmedPassword = intent.password.trim()
                    
                    state.copy(
                        name = intent.name,
                        login = if (intent.login.isBlank()) intent.email else intent.login,
                        email = intent.email,
                        password = intent.password,
                        isEnabledRegister = trimmedName.isNotBlank() && 
                                           trimmedEmail.contains("@") && 
                                           trimmedEmail.contains(".") && 
                                           trimmedPassword.isNotBlank(),
                        error = null
                    )
                }
            }
            is RegistrationIntent.Register -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                    registerUseCase(
                        login = intent.login.trim().ifBlank { intent.email.trim() },
                        password = intent.password.trim(),
                        name = intent.name.trim(),
                        email = if (intent.email.isBlank()) null else intent.email.trim()
                    ).fold(
                        onSuccess = {
                            _uiState.update { it.copy(isLoading = false, isRegistered = true) }
                        },
                        onFailure = { error ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false, 
                                    error = error.message ?: "Ошибка регистрации"
                                ) 
                            }
                        }
                    )
                }
            }
        }
    }
}
