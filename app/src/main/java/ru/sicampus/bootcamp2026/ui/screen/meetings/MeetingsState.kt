package ru.sicampus.bootcamp2026.ui.screen.meetings

import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity


interface MeetingsState {
    data class Error(val reason: String, val buttonText: String = "Попробовать ещё раз", val onClickButton: () -> Unit): MeetingsState
    data object Loading: MeetingsState
    data class Content(
        val meetings: List<MeetingEntity>
    ) : MeetingsState
}
