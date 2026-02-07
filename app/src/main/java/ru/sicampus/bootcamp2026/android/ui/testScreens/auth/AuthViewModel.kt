package ru.sicampus.bootcamp2026.android.ui.testScreens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.android.data.AuthRepository
import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.android.domain.auth.CheckAndSaveAuthUseCase
import ru.sicampus.bootcamp2026.android.domain.auth.CheckAuthFormatUseCase
import ru.sicampus.bootcamp2026.android.ui.nav.HomeRoute
import ru.sicampus.bootcamp2026.android.ui.nav.SignUpRoute

class AuthViewModel : ViewModel() {
    private val checkAuthFormatUseCase by lazy { CheckAuthFormatUseCase() }
    private val checkAndSaveAuthCodeUseCase by lazy {
        CheckAndSaveAuthUseCase(
            AuthRepository(
                authNetworkDataSource = AuthNetworkDataSource(),
                authLocalDataSource = AuthLocalDataSource
            )
        )
    }
    private val _uiState = MutableStateFlow<AuthState>(
        AuthState.Data(
            isEnabledSend = false,
            error = null
        )
    )
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val _actionFlow = MutableSharedFlow<AuthAction>()

    val actionFlow = _actionFlow.asSharedFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Send -> {
                viewModelScope.launch {
                    checkAndSaveAuthCodeUseCase.invoke(intent.login, intent.password).fold(
                        onSuccess = {
                            _actionFlow.emit(
                                AuthAction.OpenScreen(HomeRoute)
                            )
                        },
                        onFailure = { error ->
                            updateStateIfData { oldState ->
                                oldState.copy(
                                    error = error.message
                                )
                            }
                        }
                    )
                }
            }

            is AuthIntent.TextInput -> {
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

            is AuthIntent.NavigateToSignUp -> {
                viewModelScope.launch {
                    _actionFlow.emit(
                        AuthAction.OpenScreen(SignUpRoute)
                    )
                }
            }
        }
    }

    private fun updateStateIfData(lambda: (AuthState.Data) -> AuthState) {
        _uiState.update { state ->
            (state as? AuthState.Data)?.let { lambda.invoke(it) } ?: state
        }

    }
}