package com.teto.planner.presentation.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.repository.AuthRepository
import com.teto.planner.presentation.features.auth.AuthSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sharedState: AuthSharedState
) : ViewModel() {

    private val _localState = MutableStateFlow(LocalState())

    val uiState: StateFlow<RegisterUiState> = combine(
        sharedState.name,
        sharedState.login,
        sharedState.password,
        sharedState.telegramNick,
        _localState
    ) { name, login, password, telegram, local ->
        RegisterUiState.Content(
            name = name,
            login = login,
            password = password,
            telegramNick = telegram,
            isSubmitting = local.isSubmitting,
            errorMessage = local.errorMessage,
            isRegisterSuccessful = local.isSuccess
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RegisterUiState.Content()
    )

    fun onNameChange(newValue: String) {
        sharedState.name.update { newValue }
        clearError()
    }

    fun onLoginChange(newValue: String) {
        sharedState.login.update { newValue }
        clearError()
    }

    fun onPasswordChange(newValue: String) {
        sharedState.password.update { newValue }
        clearError()
    }

    fun onTelegramNickChange(newValue: String) {
        sharedState.telegramNick.update { newValue }
        clearError()
    }

    fun onRegisterClick() {
        val name = sharedState.name.value
        val login = sharedState.login.value
        val password = sharedState.password.value
        val telegram = sharedState.telegramNick.value.ifBlank { null }
        val currentState = _localState.value

        if (name.isBlank() || login.isBlank() || password.isBlank() || currentState.isSubmitting) return

        viewModelScope.launch {
            _localState.update { it.copy(isSubmitting = true, errorMessage = null) }

            repository.register(
                name = name,
                login = login,
                password = password,
                telegramNick = telegram
            ).fold(
                onSuccess = {
                    sharedState.clear()
                    _localState.update { it.copy(isSubmitting = false, isSuccess = true) }
                },
                onFailure = { error ->
                    val msg = error.localizedMessage ?: "Ошибка регистрации"
                    _localState.update { it.copy(isSubmitting = false, errorMessage = msg) }
                }
            )
        }
    }

    fun resetState() {
        _localState.update { it.copy(isSuccess = false, errorMessage = null) }
    }

    private fun clearError() {
        if (_localState.value.errorMessage != null) {
            _localState.update { it.copy(errorMessage = null) }
        }
    }

    private data class LocalState(
        val isSubmitting: Boolean = false,
        val errorMessage: String? = null,
        val isSuccess: Boolean = false
    )
}