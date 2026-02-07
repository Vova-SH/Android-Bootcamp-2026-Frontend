package ru.sicampus.bootcamp2026.ui.nav

import kotlinx.serialization.Serializable

@Serializable
data object AuthRoute: AppRoute {
    override val route: String = "auth"
}
