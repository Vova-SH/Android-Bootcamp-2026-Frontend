package ru.sicampus.bootcamp2026.android.ui.testScreens.createMeeting

import ru.sicampus.bootcamp2026.android.ui.nav.AppRoute

sealed interface CreateMeetingAction {
    data class NavigateTo(val route: AppRoute) : CreateMeetingAction
    data object MeetingCreated : CreateMeetingAction
}