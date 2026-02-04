package ru.sicampus.bootcamp2026.ui.screens.auth

import ru.sicampus.bootcamp2026.ui.screens.navigation.AuthNavigation

sealed interface AuthAction {
    data class OpenScreen(val route: AuthNavigation): AuthAction
}