package ru.sicampus.bootcamp2026.ui.screens.list

import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

sealed interface ListState {
    data class Error(val reason: String): ListState
    data object Loading: ListState
    data class UserContent(
        val users: List<UserEntity>
    ): ListState

    data class MeetingContent(
        val meetings: List<MeetingEntity>
    ): ListState
}