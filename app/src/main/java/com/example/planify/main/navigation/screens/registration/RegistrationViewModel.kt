package com.example.planify.main.navigation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.common.themes.AppTypography
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.features.settings.domain.services.SettingsService
import com.example.planify.main.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.UIntArraySerializer
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authService: AuthService,
    private val settingsService: SettingsService
): ViewModel() {
    private val _uiState = MutableStateFlow(UIState.LOADING)
    val uiState = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<AppRoute>()
    val navigation = _navigation.asSharedFlow()

    fun register(shema: RegisterUserSchema) {
        viewModelScope.launch {
            authService.register(shema)
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

    fun onIncorrectPassword() {
        viewModelScope.launch {
            _uiState.emit(UIState.INCORRECT_PASSWORD)
        }
    }

    fun resetFocusedColor() {
        viewModelScope.launch {
            _uiState.emit(UIState.LOADING)
        }
    }
}