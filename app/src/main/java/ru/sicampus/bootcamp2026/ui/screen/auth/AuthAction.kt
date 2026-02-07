package ru.sicampus.bootcamp2026.ui.screen.auth

import ru.sicampus.bootcamp2026.ui.nav.AppRoute

sealed interface AuthAction {
    data class OpenScreen(val route: AppRoute) : AuthAction
}