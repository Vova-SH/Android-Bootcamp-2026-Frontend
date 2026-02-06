package ru.sicampus.bootcamp2026.ui.navigation

/**
 * Определение маршрутов навигации приложения
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object MeetingDetail : Screen("meeting_detail/{meetingId}") {
        fun createRoute(meetingId: String) = "meeting_detail/$meetingId"
    }
    object CreateMeeting : Screen("create_meeting")
    object Profile : Screen("profile")
}

