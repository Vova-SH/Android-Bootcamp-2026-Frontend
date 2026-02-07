package ru.sicampus.bootcamp2026.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.home.GetEventsUseCase
import ru.sicampus.bootcamp2026.domain.register.RegisterUseCase



class RegisterViewModel: ViewModel() {
    private val registerUseCase = RegisterUseCase(
        userRepository = UserRepository(AuthNetworkDataSource(), UserInfoDataSource())
    )
    private val _uiState: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState.Initial)
    val uiState = _uiState.asStateFlow()

    fun register(email: String, password: String, fullName: String){
        viewModelScope.launch {
            _uiState.emit(RegisterState.Loading)

            registerUseCase.invoke(email, password, fullName).fold(
                onSuccess = { user ->
                    _uiState.emit(RegisterState.Content(user))
                },
                onFailure = { error ->
                    _uiState.emit(RegisterState.Error(error.message.orEmpty()))
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = RegisterState.Initial
    }
}

