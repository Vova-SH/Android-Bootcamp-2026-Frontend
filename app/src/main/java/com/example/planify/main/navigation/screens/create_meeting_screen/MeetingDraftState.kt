package com.example.planify.main.navigation.screens.create_meeting_screen

import com.example.planify.main.features.profiles.domain.entities.Profile
import java.time.LocalDate


data class MeetingDraftState(
    val name: String?,
    val description: String?,
    val location: String?,
    val startsAtDate: LocalDate,
    val selectedTimeSlots: List<Int>,
    val inviteUsersIds: List<Long>
) {
    companion object {
        fun empty(): MeetingDraftState = MeetingDraftState(
            name = null,
            description = null,
            location = null,
            startsAtDate = LocalDate.now(),
            selectedTimeSlots = emptyList(),
            inviteUsersIds = emptyList()
        )
    }
}
