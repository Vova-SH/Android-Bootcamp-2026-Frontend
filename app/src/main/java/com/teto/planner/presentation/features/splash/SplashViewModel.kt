package com.teto.planner.presentation.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.data.local.CredentialsHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SplashNavigationEvent {
    data object ToSchedule : SplashNavigationEvent
    data object ToLogin : SplashNavigationEvent
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val credentialsHolder: CredentialsHolder
) : ViewModel() {

    private val _navigationEvent = MutableStateFlow<SplashNavigationEvent?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val minDelay = launch { delay(500) }

            val credentials = credentialsHolder.getCredentials()

            minDelay.join()

            if (credentials != null) {
                _navigationEvent.value = SplashNavigationEvent.ToSchedule
            } else {
                _navigationEvent.value = SplashNavigationEvent.ToLogin
            }
        }
    }
}