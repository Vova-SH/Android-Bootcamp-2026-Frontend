package ru.sicampus.bootcamp2026.android.ui.testScreens.createMeeting

import ru.sicampus.bootcamp2026.android.data.source.PersonResponse

sealed interface CreateMeetingState {
    data object Loading : CreateMeetingState
    data class Data(
        val title: String = "",
        val description: String = "",
        val date: String = "",
        val startHour: String = "",
        val endHour: String = "",
        val participants: List<Participant> = emptyList(),
        val searchQuery: String = "",
        val searchResults: List<PersonResponse> = emptyList(),
        val isSearching: Boolean = false,
        val isEnabledCreate: Boolean = false,
        val error: String? = null,
        val validationHint: String? = null
    ) : CreateMeetingState
}

data class Participant(
    val id: Long,
    val email: String,
    val fullName: String
)