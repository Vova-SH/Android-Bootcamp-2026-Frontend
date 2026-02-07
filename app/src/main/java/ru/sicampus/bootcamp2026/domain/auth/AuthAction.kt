package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.ui.theme.nav.AppRoute

sealed interface AuthAction {
    data class OpenScreen(val route: AppRoute): AuthAction
}