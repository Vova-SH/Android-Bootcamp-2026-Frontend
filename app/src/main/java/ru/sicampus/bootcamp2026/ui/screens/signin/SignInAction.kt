package ru.sicampus.bootcamp2026.ui.screens.signin

import ru.sicampus.bootcamp2026.ui.navigation.Route

sealed interface SignInAction {
    data class OpenScreen(val route: Route): SignInAction
}
