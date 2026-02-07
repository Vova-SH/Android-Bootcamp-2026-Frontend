package ru.sicampus.bootcamp2026.ui.nav

import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute: AppRoute{
    override val route: String = "profile"
}