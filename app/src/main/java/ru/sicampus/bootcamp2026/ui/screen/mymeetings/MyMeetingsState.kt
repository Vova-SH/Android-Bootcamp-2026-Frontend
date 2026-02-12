package ru.sicampus.bootcamp2026.ui.screen.mymeetings

import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity

sealed interface MyMeetingsState {
    data class Error( val reason: String ): MyMeetingsState
    data object Loading: MyMeetingsState
    data class Content(
        val meetings: List<EventEntity>
    ): MyMeetingsState
}