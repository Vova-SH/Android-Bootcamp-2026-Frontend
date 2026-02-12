package com.example.planify.main.navigation.screens.settings_screen

import com.example.planify.main.features.settings.domain.entities.LocalSettings

sealed interface UIState {
    object Loading: UIState

    data class ContentData(
        val settings: LocalSettings
    ): UIState

    data class Error(val message: String): UIState
}
