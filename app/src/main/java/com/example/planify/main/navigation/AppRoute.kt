package com.example.planify.main.navigation

sealed class AppRoute(val route: String) {
    object Main : AppRoute("main")
    object Auth : AppRoute("auth")
    object Init : AppRoute("init")
    object Settings : AppRoute("settings")
    object CreateMeetingMenu : AppRoute("create_meeting")
    data class MeetingInfoMenu(val meetingId: Long) : AppRoute("meeting_info/$meetingId") {
        companion object {
            const val ARG = "meetingId"
            const val PATTERN = "meeting_info/{$ARG}"
        }
    }

    object EditProfile : AppRoute("edit_profile")

    object Notifications : AppRoute("notifications")

    object Registration : AppRoute("registration")
}