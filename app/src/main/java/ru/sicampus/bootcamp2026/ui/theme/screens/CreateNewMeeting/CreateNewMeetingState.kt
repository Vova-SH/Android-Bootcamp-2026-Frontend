package ru.sicampus.bootcamp2026.ui.theme.screens.CreateNewMeeting


sealed interface CreateNewMeetingState {
    data class Error(val reason: String): CreateNewMeetingState
    data object Loading: CreateNewMeetingState
    data class Content(
        val Name: String?,
        val Description: String?,
        val Date: String,
        val Place: String,
        val Users: List<Any>
    ):CreateNewMeetingState

}