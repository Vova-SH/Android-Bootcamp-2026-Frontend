package ru.sicampus.bootcamp2026.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.domain.auth.CheckAndSaveLoginUseCase
import ru.sicampus.bootcamp2026.domain.auth.CheckAuthFormatUseCase
import ru.sicampus.bootcamp2026.ui.nav.AppRoute

class LoginViewModel() : ViewModel() {
    private val checkAuthFormatUseCase by lazy { CheckAuthFormatUseCase() }
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

    private val _actionFlow =  MutableSharedFlow<LoginAction>()
    val actionFlow = _actionFlow.asSharedFlow()
    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Send -> {
                viewModelScope.launch {
                    checkAndSaveAuthCodeUseCase.invoke(intent.login, intent.password).
                    onSuccess {
                        _actionFlow.emit(LoginAction.OpenScreen(AppRoute.MainContentRoute))
                    }.onFailure { error ->
                        updateStateIfData { oldState ->
                            oldState.copy(
                                error = error.message
                            )

                        }

                    }
                }
            }
            is LoginIntent.TextInput -> {
                updateStateIfData { oldState ->
                    oldState.copy(
                        isEnabledSend = checkAuthFormatUseCase.invoke(
                            intent.login,
                            intent.password
                        ),
                        error = null
                    )
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