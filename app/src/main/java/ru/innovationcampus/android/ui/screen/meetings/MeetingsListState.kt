package ru.innovationcampus.android.ui.screen.meetings

import kotlinx.collections.immutable.PersistentList
import ru.innovationcampus.android.domain.list.entities.UserEntity
import ru.innovationcampus.android.domain.meetings.entities.MeetingEntity
import ru.innovationcampus.android.ui.screen.list.ListState

interface MeetingsListState {
    data class Error(val reason: String): MeetingsListState
    data object Loading: MeetingsListState
    data class Content(
        val isLastPage: Boolean,
        val meetings: PersistentList<Item>
    ): MeetingsListState

    sealed interface Item {
        data object Loading: Item
        data object Error: Item
        data class Meeting(val entity: MeetingEntity): Item
    }
}