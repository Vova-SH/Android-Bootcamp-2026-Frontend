package ru.sicampus.bootcamp2026.ui.screen.auth.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.auth.CheckAndSaveLoginUseCase
import ru.sicampus.bootcamp2026.ui.nav.AppRoute

class LoginViewModel() : ViewModel(){
    private val checkAndSaveAuthCodeUseCase by lazy { CheckAndSaveLoginUseCase(
        AuthRepository(
            authNetworkDataSource = AuthNetworkDataSource(),
            authLocalDataSource = AuthLocalDataSource
        )
    ) }
    private val _uiState = MutableStateFlow<LoginState>(
        LoginState.Data(
            isEnabledSend = false,
            error = null
        )
    )
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()
    val loginState = TextFieldState() // для стейта
    val passwordState = TextFieldState() // для стейта

    private val _actionFlow =  MutableSharedFlow<LoginAction>()
    val actionFlow = _actionFlow.asSharedFlow()
    init{
        viewModelScope.launch {
            combine(
                snapshotFlow { loginState.text },
                snapshotFlow { passwordState.text }
            ) { login, pass ->
                login.isNotEmpty() && pass.isNotEmpty()
            }.collect { isEnabled ->
                val current = _uiState.value
                if (current is LoginState.Data) {
                    _uiState.value = current.copy(isEnabledSend = isEnabled)
                }
            }
        }
    }
    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Send -> {
                val currentLogin = loginState.text.toString()
                val currentPassword = passwordState.text.toString()

                viewModelScope.launch {
                    _uiState.value = LoginState.Loading // Опционально: показать лоадер

                    checkAndSaveAuthCodeUseCase.invoke(currentLogin, currentPassword)
                        .onSuccess {
                            _actionFlow.emit(LoginAction.OpenScreen(AppRoute.MainContentRoute))
                        }
                        .onFailure { error ->
                            updateStateIfData { oldState ->
                                oldState.copy(error = error.message)
                            }
                        }
                }
            }
        }
    }
    private fun updateStateIfData(lambda: (LoginState.Data) -> LoginState) {
        _uiState.update { state ->
            (state as? LoginState.Data)?.let { lambda.invoke(it) } ?: state
        }

    }
}