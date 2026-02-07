package ru.sicampus.bootcamp2026.ui.screens.navigation

import ru.sicampus.bootcamp2026.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Schedule: BottomBarScreen(
        route = "ScheduleScreen",
        title = "Расписание",
        icon = R.drawable.clock_blue
    )

    object CreateInvite: BottomBarScreen(
        route = "CreateInviteScreen",
        title = "Создать",
        icon = R.drawable.shake
    )

    object Invites: BottomBarScreen(
        route = "InvitesScreen",
        title = "Приглашения",
        icon = R.drawable.ring
    )

    object Profile: BottomBarScreen(
        route = "ProfileScreen",
        title = "Профиль",
        icon = R.drawable.human
    )
}