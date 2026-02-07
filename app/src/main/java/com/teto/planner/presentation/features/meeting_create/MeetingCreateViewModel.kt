package com.teto.planner.presentation.features.meeting_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.model.user.UserSummary
import com.teto.planner.domain.repository.MeetingRepository
import com.teto.planner.domain.repository.RoomRepository
import com.teto.planner.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MeetingCreateViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MeetingCreateUiState>(MeetingCreateUiState.Success())
    val uiState: StateFlow<MeetingCreateUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private val pageSize = 20

    fun onSearchQueryChanged(query: String) {
        updateSuccessState {
            it.copy(
                searchQuery = query,
                searchPage = 0,
                searchResults = if (query.isBlank()) emptyList() else it.searchResults
            )
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            if (query.isBlank()) {
                updateSuccessState { it.copy(searchResults = emptyList(), isSearching = false) }
                return@launch
            }

            updateSuccessState { it.copy(isSearching = true) }

            userRepository.listUsers(query = query, page = 0, size = pageSize).onSuccess { pagedList ->
                updateSuccessState {
                    it.copy(
                        searchResults = pagedList.items,
                        searchMeta = pagedList.meta,
                        isSearching = false,
                        searchPage = 0
                    )
                }
            }.onFailure {
                updateSuccessState { it.copy(isSearching = false) }
            }
        }
    }

    fun onLoadMoreUsers() {
        val state = _uiState.value as? MeetingCreateUiState.Success ?: return
        if (!state.canLoadMoreUsers) return

        val nextPage = state.searchPage + 1

        updateSuccessState { it.copy(isLoadingMoreUsers = true) }

        viewModelScope.launch {
            userRepository.listUsers(
                query = state.searchQuery,
                page = nextPage,
                size = pageSize
            ).onSuccess { pagedList ->
                updateSuccessState { currentState ->
                    currentState.copy(
                        searchResults = currentState.searchResults + pagedList.items,
                        searchMeta = pagedList.meta,
                        searchPage = nextPage,
                        isLoadingMoreUsers = false
                    )
                }
            }.onFailure {
                updateSuccessState { it.copy(isLoadingMoreUsers = false) }
            }
        }
    }

    fun onParticipantSelected(user: UserSummary) {
        updateSuccessState { state ->
            if (state.selectedParticipants.any { it.id == user.id }) {
                return@updateSuccessState state.copy(
                    searchQuery = "",
                    searchResults = emptyList()
                )
            }

            state.copy(
                selectedParticipants = state.selectedParticipants + user,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
        updateIntersectionData()
    }

    fun onParticipantRemoved(userId: String) {
        updateSuccessState { state ->
            state.copy(selectedParticipants = state.selectedParticipants.filter { it.id != userId })
        }
        updateIntersectionData()
    }

    fun onDateSelected(date: LocalDate) {
        updateSuccessState {
            it.copy(
                selectedDate = date,
                intersectionResponse = null,
                selectedHour = null,
                availableRooms = emptyList(),
                selectedRoomId = null,
                roomsMeta = null
            )
        }
        updateIntersectionData()
    }

    private fun updateIntersectionData() {
        val state = _uiState.value as? MeetingCreateUiState.Success ?: return
        if (state.selectedParticipants.isEmpty()) {
            updateSuccessState { it.copy(intersectionResponse = null) }
            return
        }

        viewModelScope.launch {
            meetingRepository.getIntersection(
                date = state.selectedDate,
                userIds = state.selectedParticipants.map { it.id }
            ).onSuccess { response ->
                updateSuccessState { it.copy(intersectionResponse = response) }
            }
        }
    }

    fun onHourSelected(hour: Int) {
        updateSuccessState { it.copy(selectedHour = hour, selectedRoomId = null) }
        updateAvailableRooms()
    }

    private fun updateAvailableRooms() {
        val state = _uiState.value as? MeetingCreateUiState.Success ?: return
        val hour = state.selectedHour ?: return

        updateSuccessState { it.copy(isLoadingRooms = true) }
        viewModelScope.launch {
            roomRepository.listAvailableRooms(
                date = state.selectedDate,
                startHour = hour
            ).onSuccess { pagedList ->
                updateSuccessState {
                    it.copy(
                        availableRooms = pagedList.items,
                        roomsMeta = pagedList.meta,
                        isLoadingRooms = false
                    )
                }
            }.onFailure {
                updateSuccessState { it.copy(isLoadingRooms = false) }
            }
        }
    }

    fun onRoomSelected(roomId: String) {
        updateSuccessState { it.copy(selectedRoomId = roomId) }
    }

    fun onTitleChanged(title: String) {
        updateSuccessState { it.copy(title = title) }
    }

    fun onDescriptionChanged(description: String) {
        updateSuccessState { it.copy(description = description) }
    }

    fun onCreateMeeting(onSuccess: () -> Unit) {
        val state = _uiState.value as? MeetingCreateUiState.Success ?: return
        if (!state.canSubmit) return

        updateSuccessState { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            meetingRepository.createMeeting(
                date = state.selectedDate,
                startTimeHour = state.selectedHour!!,
                durationHours = state.durationHours,
                title = state.title,
                description = state.description.takeIf { it.isNotBlank() },
                roomId = state.selectedRoomId,
                participantIds = state.selectedParticipants.map { it.id }
            ).onSuccess {
                onSuccess()
            }.onFailure { error ->
                updateSuccessState { it.copy(isSubmitting = false) }
                _uiState.update { MeetingCreateUiState.Error(error.message ?: "Ошибка") }
            }
        }
    }

    private fun updateSuccessState(update: (MeetingCreateUiState.Success) -> MeetingCreateUiState.Success) {
        _uiState.update { if (it is MeetingCreateUiState.Success) update(it) else it }
    }
}