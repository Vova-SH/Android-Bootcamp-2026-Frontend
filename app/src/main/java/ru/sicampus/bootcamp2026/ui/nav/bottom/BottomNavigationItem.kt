package ru.sicampus.bootcamp2026.ui.nav.bottom

import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.nav.AppRoute

sealed class BottomNavigationItem(val title: String, val iconId: Int, val route: AppRoute) {
    object ScreenInbox : BottomNavigationItem("Входящие", R.drawable.ic_inbox, AppRoute.InboxRoute)
    object ScreenMeetings : BottomNavigationItem("Встречи", R.drawable.ic_meetings, AppRoute.MeetingsRoute)
    object ScreenProfile : BottomNavigationItem("Профиль", R.drawable.ic_profile, AppRoute.ProfileRoute)
}