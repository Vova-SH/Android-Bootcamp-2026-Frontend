package ru.sicampus.bootcamp2026.ui.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.sicampus.bootcamp2026.data.repository.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.auth.AuthState
import ru.sicampus.bootcamp2026.domain.auth.GetAuthStatusUseCase

class AuthViewModel(
) : ViewModel() {
    private val getAuthStatusUseCase = GetAuthStatusUseCase(
        authRepository = AuthRepository(
            authNetworkDataSource = AuthNetworkDataSource(),
            authLocalDataSource = AuthLocalDataSource
        )
    )
    val authState: StateFlow<AuthState> = getAuthStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.Loading
        )
}