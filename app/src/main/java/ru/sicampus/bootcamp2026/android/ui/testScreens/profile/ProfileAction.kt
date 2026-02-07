package ru.sicampus.bootcamp2026.android.ui.testScreens.profile

import ru.sicampus.bootcamp2026.android.ui.nav.AppRoute

sealed interface ProfileAction {
    data class NavigateTo(val route: AppRoute) : ProfileAction
}