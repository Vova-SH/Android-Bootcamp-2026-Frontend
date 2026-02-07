package ru.sicampus.bootcamp2026

sealed interface ViewModelState {
    data class  Error(val reason: String): ViewModelState
    data object Loading: ViewModelState
    data object Login: ViewModelState
    data object Invitations: ViewModelState
    data object TimeTable: ViewModelState
    data object Profile: ViewModelState
    data object MeetingResponse: ViewModelState
    data object CreateMeeting:ViewModelState
}