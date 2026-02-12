package ru.sicampus.bootcamp2026.ui.screens.meetings

import kotlinx.collections.immutable.PersistentList
import ru.sicampus.bootcamp2026.domain.users.entities.MeetingEntity
import ru.sicampus.bootcamp2026.domain.users.entities.UserEntity

sealed interface MeetingsState {
    data class Error(val reason: String): MeetingsState
    data object Loading: MeetingsState
    data class Content(
        val isLastPage: Boolean,
        val meetings: PersistentList<Item>
    ): MeetingsState

    sealed interface Item {
        data object Loading: Item
        data object Error: Item
        data class Meeting(val meetingEntity: MeetingEntity): Item
    }
}