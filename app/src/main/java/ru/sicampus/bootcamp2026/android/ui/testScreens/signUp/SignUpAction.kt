package ru.sicampus.bootcamp2026.android.ui.testScreens.signUp

import ru.sicampus.bootcamp2026.android.ui.nav.AppRoute

sealed interface SignUpAction {
    data class OpenScreen(val route: AppRoute) : SignUpAction
    data object NavigateBack : SignUpAction
}