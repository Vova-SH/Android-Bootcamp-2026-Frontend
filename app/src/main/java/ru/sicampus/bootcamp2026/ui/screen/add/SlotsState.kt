package ru.sicampus.bootcamp2026.ui.screen.add

import ru.sicampus.bootcamp2026.domain.add.entities.TimeSlotEntity


sealed interface SlotsState {
    data class Error( val reason: String ): SlotsState
    data object Loading: SlotsState
    data class Content(
        val timeSlots: List<TimeSlotEntity>
    ): SlotsState
}