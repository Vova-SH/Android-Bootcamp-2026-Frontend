package com.teto.planner.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
) {
    object Schedule : NavigationItem(
        screen = Screen.Schedule,
        label = "Расписание",
        icon = Icons.Default.DateRange
    )

    object Invitations : NavigationItem(
        screen = Screen.Invitations,
        label = "Приглашения",
        icon = Icons.Default.Email
    )
}

val bottomNavItems = listOf(
    NavigationItem.Schedule,
    NavigationItem.Invitations
)