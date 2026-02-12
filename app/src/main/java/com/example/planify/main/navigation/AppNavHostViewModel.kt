package com.example.planify.main.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.features.auth.domain.entities.AuthState
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.features.settings.domain.services.SettingsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavHostViewModel @Inject constructor(
    private val authService: AuthService,
    //private val settingsService: SettingsService
) : ViewModel() {
    private val _events = MutableSharedFlow<NavHostUIEffect>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effects = _events.asSharedFlow()

    init {
        getAuthState()
    }

    fun getAuthState() {
        var previousAuthState: AuthState? = null

        viewModelScope.launch {
            authService.authStateFlow
                .collect { state ->
                    when (state) {
                    is AuthState.Unauthenticated -> {
                        Log.d("AUTHSTATE", "Unauthenticated")

                        if (previousAuthState !is AuthState.Authenticated) {
                            _events.emit(NavHostUIEffect.Navigate(AppRoute.Auth))
                        } else {
                            _events.emit(NavHostUIEffect.ShowDialog(DialogType.AuthError))
                        }
                    }

                    is AuthState.Authenticated -> {
                        Log.d("AUTHSTATE", "Authenticated")

                        _events.emit(NavHostUIEffect.Navigate(AppRoute.Main))
                    }
                    is AuthState.Loading -> {}
                }

                previousAuthState = state
            }
        }
    }
}
