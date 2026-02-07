package ru.sicampus.bootcamp2026.android.ui.testScreens.auth

import ru.sicampus.bootcamp2026.android.ui.nav.AppRoute

sealed interface AuthAction {
    data class OpenScreen(val route: AppRoute): AuthAction
}