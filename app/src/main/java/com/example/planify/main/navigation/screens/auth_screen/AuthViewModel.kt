package com.example.planify.main.navigation.screens.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.features.settings.domain.services.SettingsService
import com.example.planify.main.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val settingsService: SettingsService
): ViewModel() {
    private val _uiState = MutableStateFlow(UIState.LOADING)

    val uiState = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<AppRoute>()
    val navigation = _navigation.asSharedFlow()

    fun authorize(email: String, password: String) {
        viewModelScope.launch {
            authService.login(
                email = email,
                password = password
            )
                .onSuccess {
                    settingsService.setIsFirstStart(false)
                    _navigation.emit(AppRoute.Main)
                    _uiState.emit(UIState.LOADING)
                }
                .onFailure {
                    _uiState.emit(UIState.DATA_INCORRECT)
                }
        }
    }

    fun resetFocusedColor() {
        viewModelScope.launch {
            _uiState.emit(UIState.LOADING)
        }
    }
}