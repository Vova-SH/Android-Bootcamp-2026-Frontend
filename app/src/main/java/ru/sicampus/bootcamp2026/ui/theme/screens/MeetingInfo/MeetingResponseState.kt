package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import ru.sicampus.bootcamp2026.domain.entities.UserEntity

sealed interface MeetingResponseState {
    data class Error(val reason: String) : MeetingResponseState
    data object Loading : MeetingResponseState
    data class Content(val users: List<UserEntity>) : MeetingResponseState
}