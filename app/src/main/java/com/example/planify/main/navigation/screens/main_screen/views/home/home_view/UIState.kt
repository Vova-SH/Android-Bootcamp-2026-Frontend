package com.example.planify.main.navigation.screens.main_screen.views.home.home_view

import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import java.time.LocalDate

data class UIState(
    val meetingsInfo: ResourceState<Map<LocalDate, List<MeetingContext>>>,
    val meetingsInfoShort: ResourceState<Map<LocalDate, Int>>
) {
    companion object {
        fun empty(): UIState = UIState(
            meetingsInfo = ResourceState.Idle,
            meetingsInfoShort = ResourceState.Idle
        )
    }
}
