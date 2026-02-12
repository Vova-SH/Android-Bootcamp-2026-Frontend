package ru.sicampus.bootcamp2026.ui.root.nav


import ru.sicampus.bootcamp2026.R

object ItemsNav {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Расписание",
            icon = R.drawable.schedule,
            route = "schedule"
        ),
        BottomNavItem(
            label = "Входящие",
            icon = R.drawable.mail,
            route = "incoming"
        ),
        BottomNavItem(
            label = "Профиль",
            icon = R.drawable.profile,
            route = "profile"
        ),
        BottomNavItem(
            label = "book",
            route = "booking"
        ),
        BottomNavItem(
            label = "details",
            route = "details"
        )
    )

}