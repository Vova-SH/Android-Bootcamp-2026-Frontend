package ru.sicampus.bootcamp2026.ui.nav

import kotlinx.serialization.Serializable


@Serializable
data object MyMeetingRoute: AppRoute{
    override val route: String = "mymeeting"
}