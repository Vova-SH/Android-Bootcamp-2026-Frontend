package ru.sicampus.bootcamp2026.ui.nav

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable data object LoginRoute : AppRoute

    @Serializable data object RegisterRoute : AppRoute
    @Serializable data object MainContentRoute : AppRoute
    @Serializable object InboxRoute : AppRoute
    @Serializable object MeetingsRoute : AppRoute
    @Serializable object ProfileRoute : AppRoute
}

