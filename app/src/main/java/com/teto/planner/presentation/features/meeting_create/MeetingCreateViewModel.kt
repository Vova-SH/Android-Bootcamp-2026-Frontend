package com.teto.planner.presentation.features.meeting_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.model.common.PagedList
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    init {
        loadUser()
        observeRecentContacts()
    }

    private fun observeRecentContacts() {
        userRepository.getRecentUsers()
            .onEach { recentList ->
                updateSuccessState { it.copy(recentUsers = recentList) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadUser() {
        viewModelScope.launch {
            userRepository.getMe().onSuccess { userMe ->
                updateSuccessState { it.copy(userMe = userMe) }
            }.onFailure {
                updateSuccessState { it.copy(userMe = null) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val trimmedQuery = query.trim()

        updateSuccessState {
            it.copy(
                searchQuery = query,
                searchPage = 0,
                searchResults = if (query.isBlank()) emptyList() else it.searchResults
            )
        }

        val state = _uiState.value as? MeetingCreateUiState.Success ?: return
        val userMeId = state.userMe?.id

        searchJob?.cancel()

        if (query.isBlank()) {
            updateSuccessState { it.copy(isSearching = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)

            updateSuccessState { it.copy(isSearching = true) }

            userRepository.listUsers(query = trimmedQuery, page = 0, size = pageSize)
                .onSuccess { pagedList ->
                    val foundMe = pagedList.items.any { it.id == userMeId}
                    val filteredList = pagedList.items.filterNot { it.id == userMeId }
                    val filteredPagedList = PagedList(
                        items = filteredList,
                        meta = pagedList.meta
                    )
                    updateSuccessState {
                        it.copy(
                            searchResults = filteredPagedList.items,
                            searchMeta = filteredPagedList.meta,
                            isSearching = false,
                            searchPage = 0,
                            isUserMeDeleted = foundMe
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

        val userMeId = state.userMe?.id
        val nextPage = state.searchPage + 1
        val trimmedQuery = state.searchQuery.trim()

        if (trimmedQuery.isEmpty()) return

        updateSuccessState { it.copy(isLoadingMoreUsers = true) }

        viewModelScope.launch {
            userRepository.listUsers(
                query = trimmedQuery,
                page = nextPage,
                size = pageSize
            ).onSuccess { pagedList ->
                if (pagedList.items.isEmpty()) {
                    updateSuccessState { it.copy(isLoadingMoreUsers = false) }
                    return@onSuccess
                }
                val foundMeOnThisPage = pagedList.items.any { it.id == userMeId }
                val filteredList = pagedList.items.filterNot { it.id == userMeId }

                updateSuccessState { currentState ->
                    currentState.copy(
                        searchResults = currentState.searchResults + filteredList,
                        searchMeta = pagedList.meta,
                        searchPage = nextPage,
                        isLoadingMoreUsers = false,
                        isUserMeDeleted = currentState.isUserMeDeleted || foundMeOnThisPage
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
                state.copy(
                    searchQuery = "",
                    searchResults = emptyList()
                )
            } else {
                state.copy(
                    selectedParticipants = state.selectedParticipants + user,
                    searchQuery = "",
                    searchResults = emptyList()
                )
            }
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
        viewModelScope.launch {
            val state = _uiState.value as? MeetingCreateUiState.Success
            if (state == null || state.selectedParticipants.isEmpty()) {
                updateSuccessState { it.copy(intersectionResponse = null) }
                return@launch
            }

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
        viewModelScope.launch {
            val state = _uiState.value as? MeetingCreateUiState.Success
            val hour = state?.selectedHour

            if (state == null || hour == null) return@launch

            updateSuccessState { it.copy(isLoadingRooms = true) }

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
                userRepository.saveRecentUsers(state.selectedParticipants)
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