package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import kotlinx.coroutines.flow.MutableStateFlow
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

sealed interface MeetingInfoState {
    data class Error(val reason: String) : MeetingInfoState
    data object Loading : MeetingInfoState
    data class Content(val users: List<UserEntity>): MeetingInfoState
}