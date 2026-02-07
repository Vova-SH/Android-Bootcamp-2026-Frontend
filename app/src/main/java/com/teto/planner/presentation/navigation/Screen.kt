package com.teto.planner.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen(Routes.LOGIN)
    object Register : Screen(Routes.REGISTER)

    object Schedule : Screen(Routes.SCHEDULE)
    object Invitations : Screen(Routes.INVITATIONS)

    object Profile : Screen(Routes.PROFILE)
    object MeetingCreate : Screen(Routes.MEETING_CREATE)

    object Routes {
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val SCHEDULE = "schedule"
        const val INVITATIONS = "invitations"
        const val PROFILE = "profile"
        const val MEETING_CREATE = "meeting_create"
    }
}