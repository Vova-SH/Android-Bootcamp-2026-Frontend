package com.teto.planner.presentation.features.meeting_create

import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.model.meeting.IntersectionResponse
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.model.user.UserSummary
import java.time.LocalDate

sealed interface MeetingCreateUiState {
    data object Loading : MeetingCreateUiState

    data class Success(
        val searchQuery: String = "",
        val searchResults: List<UserSummary> = emptyList(),
        val recentUsers: List<UserSummary> = emptyList(),

        val searchMeta: PageMeta? = null,
        val searchPage: Int = 0,
        val isSearching: Boolean = false,
        val isLoadingMoreUsers: Boolean = false,

        val isUserMeDeleted: Boolean = false,

        val userMe: UserMe? = null,
        val selectedParticipants: List<UserSummary> = emptyList(),

        val selectedDate: LocalDate = LocalDate.now(),
        val intersectionResponse: IntersectionResponse? = null,
        val selectedHour: Int? = null,

        val availableRooms: List<Room> = emptyList(),
        val roomsMeta: PageMeta? = null,
        val selectedRoomId: String? = null,
        val isLoadingRooms: Boolean = false,

        val title: String = "",
        val description: String = "",
        val durationHours: Int = 1,

        val isSubmitting: Boolean = false
    ) : MeetingCreateUiState {
        val selectedRoom: Room?
            get() = availableRooms.find { it.id == selectedRoomId }
        val isCapacityValid: Boolean
            get() = selectedRoom?.let { room ->
                (selectedParticipants.size + 1) <= room.capacity
            } ?: true

        val canSubmit: Boolean get() = title.isNotBlank() &&
                selectedHour != null &&
                selectedParticipants.isNotEmpty() &&
                !isSubmitting &&
                isCapacityValid

        val canLoadMoreUsers: Boolean
            get() = !isSearching &&
                    !isLoadingMoreUsers &&
                    searchMeta != null &&
                    searchMeta.hasNext
    }

    data class Error(val message: String) : MeetingCreateUiState
}