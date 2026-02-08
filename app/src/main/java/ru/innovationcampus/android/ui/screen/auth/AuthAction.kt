package ru.innovationcampus.android.ui.screen.auth

import ru.innovationcampus.android.ui.nav.AppRoute

sealed interface AuthAction {
    data class OpenScreen(val route: AppRoute): AuthAction
}