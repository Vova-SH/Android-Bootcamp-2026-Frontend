package ru.sicampus.bootcamp2026.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.data.SignInRepository
import ru.sicampus.bootcamp2026.data.source.SignInLocalDataSource
import ru.sicampus.bootcamp2026.data.source.SignInNetworkDataSource
import ru.sicampus.bootcamp2026.domain.signin.CheckAndSaveSignInUseCase
import ru.sicampus.bootcamp2026.domain.signin.CheckSignInFormatUseCase
import ru.sicampus.bootcamp2026.domain.signin.CurrentUserUseCase
import ru.sicampus.bootcamp2026.ui.navigation.MeetingsScreen
import ru.sicampus.bootcamp2026.ui.navigation.SignUp
import ru.sicampus.bootcamp2026.ui.navigation.TimeTable
import ru.sicampus.bootcamp2026.ui.navigation.UsersScreen
import ru.sicampus.bootcamp2026.ui.screens.signin.SignInAction.OpenScreen

class SignInViewModel: ViewModel() {
    private val checkSignInFormatUseCase by lazy { CheckSignInFormatUseCase() }
    private val signInRepository by lazy { SignInRepository(
        signInLocalDataSource = SignInLocalDataSource,
        signInNetworkDataSource = SignInNetworkDataSource())
    }
    private val checkAndSaveSignInUseCase by lazy {
        CheckAndSaveSignInUseCase(
            signInRepository
        )
    }
    private val currentUserUseCase by lazy { CurrentUserUseCase(
        signInRepository
    ) }
    private val _uiState = MutableStateFlow<SignInState>(
        SignInState.Data(
            //не знаю надо ли вообще реализовывать userLoggedIn
            userLoggedIn = true,
            isEnabledSend = false,
            error = null,
        )
    )
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    private val _actionFlow = MutableSharedFlow<SignInAction>()

    val actionFlow = _actionFlow.asSharedFlow()

    fun onIntent(intent: SignInIntent) {
        when (intent) {
            is SignInIntent.Send -> {
                viewModelScope.launch {
                    checkAndSaveSignInUseCase(intent.login, intent.password).fold(
                        onSuccess = {
//                            currentUserUseCase().fold(
//                                onSuccess = { App.user = it },
//                                onFailure = { error("User is not found") }
//                            )
                            _actionFlow.emit(
                                OpenScreen(TimeTable)
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

            is SignInIntent.TextInput -> {
                updateStateIfData { oldState ->
                    oldState.copy(
                        isEnabledSend = checkSignInFormatUseCase(
                            intent.login,
                            intent.password,
                        )
                    )
                }
            }

            is SignInIntent.Register -> {
                viewModelScope.launch {
                    _actionFlow.emit(
                        OpenScreen(SignUp)
                    )
                }
            }
        }
    }
    private fun updateStateIfData(lambda: (SignInState.Data) -> SignInState) {
        _uiState.update { state ->
            (state as? SignInState.Data)?.let { lambda(it) } ?: state
        }
    }
}