package ru.sicampus.bootcamp2026.ui.screens.auth.signup

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
import ru.sicampus.bootcamp2026.domain.usecase.auth.RegisterUseCase


class SignUpViewModel(private val regUseCase: RegisterUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _navigationEvents: Channel<ActionState> = Channel()
    val navigationEvents: Flow<ActionState> = _navigationEvents.receiveAsFlow()

    fun register() {
        if (!validateInput()) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = regUseCase(
                email = _uiState.value.email,
                password = _uiState.value.password,
                firstName = _uiState.value.name,
                secondName = _uiState.value.surname
            )

            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isSignUpSuccess = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isSignUpSuccess = false,
                            errorMessage = error.message ?: "Ошибка регистрации"
                        )
                    }
                }
            )
        }
    }

    fun navigate(actionState: ActionState) {
        viewModelScope.launch {
            _navigationEvents.send(actionState)
        }
    }

    fun resetSignUpState() {
        _uiState.update { it.copy(isSignUpSuccess = false) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onPassword2Change(password2: String) {
        _uiState.update { it.copy(password2 = password2, errorMessage = null) }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, errorMessage = null) }
    }

    fun onSurnameChange(surname: String) {
        _uiState.update { it.copy(surname = surname, errorMessage = null) }
    }

    private fun validateInput(): Boolean {
        val email = _uiState.value.email
        val password = _uiState.value.password
        val password2 = _uiState.value.password2
        val name = _uiState.value.name
        val surname = _uiState.value.surname

        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите имя") }
            return false
        }
        if (name.length > 20) {
            _uiState.update { it.copy(errorMessage = "Не более 20 символов") }
            return false
        }
        if (surname.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите фамилию") }
            return false
        }
        if (surname.length > 20) {
            _uiState.update { it.copy(errorMessage = "Не более 20 символов") }
            return false
        }
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите email") }
            return false
        }
        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Неверный email") }
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
        if (password2.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Повторите пароль") }
            return false
        }
        if (password != password2) {
            _uiState.update { it.copy(errorMessage = "Пароли не совпадают") }
            return false
        }
        return true
    }
}