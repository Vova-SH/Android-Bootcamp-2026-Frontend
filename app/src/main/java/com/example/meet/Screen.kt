package com.example.meet

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object MainMeet : Screen("main_meet")
    data object Notifications : Screen("notifications")
    data object Profile : Screen("profile")
    data object InfoProfile : Screen("info_profile")
    data object Schedule : Screen("schedule")
    data object InvitationsList : Screen("invitations_list")
    data object CreateMeeting : Screen("create_meeting")
    data object MeetingDetails : Screen("meeting_details/{id}")
    data object InvitationDetails : Screen("invitation_details/{id}")
}
