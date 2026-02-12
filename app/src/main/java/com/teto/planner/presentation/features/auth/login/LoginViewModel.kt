package com.teto.planner.presentation.features.auth.login

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
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sharedState: AuthSharedState
) : ViewModel() {

    private val _localState = MutableStateFlow(LocalState())

    val uiState: StateFlow<LoginUiState> = combine(
        sharedState.login,
        sharedState.password,
        _localState
    ) { login, password, local ->
        LoginUiState.Content(
            login = login,
            password = password,
            isSubmitting = local.isSubmitting,
            errorMessage = local.errorMessage,
            isLoginSuccessful = local.isSuccess
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginUiState.Content()
    )

    fun onLoginInputChange(newValue: String) {
        sharedState.login.update { newValue }
        clearError()
    }

    fun onPasswordInputChange(newValue: String) {
        sharedState.password.update { newValue }
        clearError()
    }

    fun onLoginClick() {
        val currentLogin = sharedState.login.value
        val currentPassword = sharedState.password.value
        val currentState = _localState.value

        if (currentLogin.isBlank() || currentPassword.isBlank() || currentState.isSubmitting) return

        viewModelScope.launch {
            _localState.update { it.copy(isSubmitting = true, errorMessage = null) }

            repository.login(currentLogin, currentPassword)
                .fold(
                    onSuccess = {
                        sharedState.clear()
                        _localState.update { it.copy(isSubmitting = false, isSuccess = true) }
                    },
                    onFailure = { error ->
                        val msg = error.localizedMessage ?: "Ошибка входа"
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