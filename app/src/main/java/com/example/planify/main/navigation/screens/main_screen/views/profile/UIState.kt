package com.example.planify.main.navigation.screens.main_screen.views.profile

import com.example.planify.main.features.auth.domain.entities.UserPrivate
import com.example.planify.main.features.profiles.domain.entities.Profile

sealed interface UIState {
    object Loading: UIState

    data class ContentData(
        val profile: Profile,
        val user: UserPrivate
    ): UIState

    data class Error(val message: String): UIState
}
