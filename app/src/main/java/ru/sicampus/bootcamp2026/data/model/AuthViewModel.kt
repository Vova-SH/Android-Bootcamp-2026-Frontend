package ru.sicampus.bootcamp2026.data.model

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.auth.AuthRepository
import ru.sicampus.bootcamp2026.data.auth.NetworkClient
import ru.sicampus.bootcamp2026.data.auth.SessionManager
import ru.sicampus.bootcamp2026.data.auth.TokenStorage

enum class AuthScreenType {
    LOGIN, REGISTER, FORGOT_PASSWORD, EMAIL_CONFIRM, RESET_PASSWORD
}

data class AuthUiState(
    val isAuthed: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val screenType: AuthScreenType = AuthScreenType.LOGIN,
    val emailForConfirmation: String = ""
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    init {
        viewModelScope.launch {
            SessionManager.logoutSignal.collect {
                logout()
            }
        }
    }

    private val tokenStorage = TokenStorage(app.applicationContext)
    private val repo = AuthRepository(NetworkClient.createAuthApi(), tokenStorage)

    private val _state = MutableStateFlow(
        AuthUiState(isAuthed = !tokenStorage.getToken().isNullOrBlank())
    )
    val state: StateFlow<AuthUiState> = _state

    fun switchScreen(type: AuthScreenType) {
        _state.value = _state.value.copy(screenType = type, error = null, successMessage = null)
    }

    fun login(email: String, pass: String) {
        if (!isValidEmail(email)) {
            setError("Введите корректную почту")
            return
        }
        if (pass.isBlank()) {
            setError("Введите пароль")
            return
        }
        launchRequest {
            repo.login(email, pass)
            if (tokenStorage.getToken() != null) {
                _state.value = _state.value.copy(isAuthed = true)
            }
        }
    }

    fun register(name: String, email: String, pass: String, position: String) {
        if (name.isBlank()) {
            setError("Введите ФИО")
            return
        }
        if (!isValidEmail(email)) {
            setError("Введите корректную почту")
            return
        }
        if (pass.length < 6) {
            setError("Пароль должен быть не менее 6 символов")
            return
        }
        launchRequest {
            val res = repo.register(name, email, pass, position)
            res.onSuccess {
                _state.value = _state.value.copy(
                    screenType = AuthScreenType.EMAIL_CONFIRM,
                    emailForConfirmation = email,
                    error = null
                )
            }.onFailure {
                setError(it.message ?: "Ошибка регистрации")
            }
        }
    }

    fun confirmEmail(token: String) {
        if (token.length < 6) {
            setError("Код должен состоять из 6 цифр")
            return
        }
        launchRequest {
            repo.confirmEmail(token)
                .onSuccess {
                    _state.value = _state.value.copy(
                        successMessage = "Почта подтверждена! Войдите.",
                        screenType = AuthScreenType.LOGIN
                    )
                }
                .onFailure { setError(it.message ?: "Неверный код") }
        }
    }

    fun forgotPassword(email: String) {
        if (!isValidEmail(email)) {
            setError("Введите корректную почту")
            return
        }
        launchRequest {
            val res = repo.forgotPassword(email)
            res.onSuccess {
                _state.value = _state.value.copy(
                    emailForConfirmation = email,
                    screenType = AuthScreenType.RESET_PASSWORD
                )
            }.onFailure {
                setError(it.message ?: "Ошибка отправки")
            }
        }
    }

    fun resetPassword(token: String, newPass: String) {
        if (token.isBlank()) {
            setError("Введите токен из письма")
            return
        }
        if (newPass.length < 6) {
            setError("Новый пароль должен быть не менее 6 символов")
            return
        }
        launchRequest {
            val res = repo.resetPassword(token, newPass)
            res.onSuccess {
                _state.value = _state.value.copy(
                    successMessage = "Пароль изменен!",
                    screenType = AuthScreenType.LOGIN
                )
            }.onFailure {
                setError(it.message ?: "Ошибка сброса")
            }
        }
    }

    fun logout() {
        repo.logout()
        _state.value = AuthUiState(
            isAuthed = false,
            screenType = AuthScreenType.LOGIN
        )
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun launchRequest(block: suspend () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                block()
            } catch (e: Exception) {
                setError(e.message ?: "Ошибка сети")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun setError(msg: String) {
        _state.value = _state.value.copy(error = msg, isLoading = false)
    }
}