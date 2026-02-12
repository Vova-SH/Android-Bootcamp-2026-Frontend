package com.example.planify.main.navigation.screens.settings_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.common.entities.ThemeId
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.features.settings.domain.services.SettingsService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val settingsService: SettingsService
): ViewModel() {
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)

    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading)

            settingsService.settingsFlow.collect { data ->
                    _uiState.emit(
                        UIState.ContentData(settings = data)
                    )
            }
        }
    }

    fun setTheme(theme: ThemeId) {
        viewModelScope.launch {
            settingsService.setTheme(theme)
        }
    }

    fun setNotificationsEnable(enabled: Boolean) {
        viewModelScope.launch {
            settingsService.setNotificationsEnabled(enabled)
        }
    }

}