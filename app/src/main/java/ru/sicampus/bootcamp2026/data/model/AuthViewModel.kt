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
import ru.sicampus.bootcamp2026.data.auth.TokenStorage

data class AuthUiState(
    val isAuthed: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)



class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val tokenStorage = TokenStorage(app.applicationContext)
    private val repo = AuthRepository(NetworkClient.createAuthApi(), tokenStorage)

    private val _state = MutableStateFlow(
        AuthUiState(isAuthed = !tokenStorage.getToken().isNullOrBlank())
    )
    val state: StateFlow<AuthUiState> = _state

    fun register(fullName: String, email: String, password: String) {
        val err = validate(fullName, email, password)
        if (err != null) {
            _state.value = _state.value.copy(error = err)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            val result = repo.register(fullName, email, password)
            _state.value = _state.value.copy(
                loading = false,
                error = result.exceptionOrNull()?.message,
                isAuthed = result.isSuccess && !tokenStorage.getToken().isNullOrBlank()
            )
        }
    }

    fun login(email: String, password: String) {
        val e = email.trim()

        if (e.isEmpty()) {
            _state.value = _state.value.copy(error = "Введите email")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            _state.value = _state.value.copy(error = "Некорректный email")
            return
        }

        if (password.isEmpty()) {
            _state.value = _state.value.copy(error = "Введите пароль")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)

            val result = repo.login(e, password)

            _state.value = _state.value.copy(
                loading = false,
                error = result.exceptionOrNull()?.message,
                isAuthed = result.isSuccess && !tokenStorage.getToken().isNullOrBlank()
            )
        }
    }

    fun logout() {
        tokenStorage.clearToken()
        _state.value = _state.value.copy(isAuthed = false)
    }

    private fun validate(fullName: String, email: String, password: String): String? {
        if (fullName.trim().length < 2) return "Введите ФИО (минимум 2 символа)"
        val e = email.trim()
        if (e.isEmpty()) return "Введите email"
        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) return "Некорректный email"
        if (password.length < 6) return "Пароль должен быть не короче 6 символов"
        return null
    }
}
