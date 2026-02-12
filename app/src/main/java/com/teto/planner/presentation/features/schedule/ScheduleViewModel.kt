package com.teto.planner.presentation.features.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: MeetingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private val loadedMonths = mutableSetOf<YearMonth>()
    private var currentUserId: String? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getCurrentUserId()
                .onSuccess { id ->
                    currentUserId = id
                    loadMeetingsForMonth(LocalDate.now())
                }
                .onFailure {
                    _uiState.update { ScheduleUiState.Error("Не удалось загрузить профиль пользователя") }
                }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { currentState ->
            if (currentState is ScheduleUiState.Success) {
                val newMeetingsForDate = currentState.meetings.filter { it.date == date }
                currentState.copy(
                    selectedDate = date,
                    meetingsForSelectedDate = newMeetingsForDate
                )
            } else {
                currentState
            }
        }
    }

    fun refresh() {
        val currentState = uiState.value
        if (currentState is ScheduleUiState.Success) {
            val monthToRefresh = YearMonth.from(currentState.selectedDate)
            loadedMonths.remove(monthToRefresh)
            loadMeetingsForMonth(currentState.selectedDate, isPullToRefresh = true)
        } else {
            loadInitialData()
        }
    }

    fun loadMeetingsForMonth(date: LocalDate, isPullToRefresh: Boolean = false) {
        val userId = currentUserId ?: return
        val monthToLoad = YearMonth.from(date)

        if (!isPullToRefresh && loadedMonths.contains(monthToLoad)) {
            return
        }

        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is ScheduleUiState.Success) {
                    currentState.copy(
                        isRefreshing = isPullToRefresh,
                        isLoading = !isPullToRefresh
                    )
                } else {
                    ScheduleUiState.Loading
                }
            }

            val currentStateForMerge = _uiState.value
            val currentMeetings = if (currentStateForMerge is ScheduleUiState.Success) {
                currentStateForMerge.meetings
            } else {
                emptyList()
            }

            val currentlySelectedDate = (currentStateForMerge as? ScheduleUiState.Success)?.selectedDate

            val newMeetings = mutableListOf<Meeting>()
            var currentPage = 0
            var hasNextPage = true
            var errorOccurred: String? = null
            val pageSize = 50

            val startDate = date.withDayOfMonth(1)
            val endDate = date.withDayOfMonth(date.lengthOfMonth())

            while (hasNextPage) {
                val result = repository.getMeetings(
                    startDate = startDate,
                    endDate = endDate,
                    page = currentPage,
                    size = pageSize
                )

                result.onSuccess { pagedList ->
                    newMeetings.addAll(pagedList.items)
                    hasNextPage = pagedList.meta.hasNext
                    currentPage++
                }.onFailure { error ->
                    hasNextPage = false
                    errorOccurred = error.message
                }

                if (currentPage > 20) hasNextPage = false
            }

            if (errorOccurred != null && currentMeetings.isEmpty() && newMeetings.isEmpty()) {
                _uiState.update { ScheduleUiState.Error(errorOccurred!!) }
            } else {
                if (errorOccurred == null) {
                    loadedMonths.add(monthToLoad)
                }

                val updatedList = (newMeetings + currentMeetings).distinctBy { it.id }

                val targetDate = currentlySelectedDate ?: date

                val meetingsForDate = updatedList.filter { it.date == targetDate }

                _uiState.update {
                    ScheduleUiState.Success(
                        meetings = updatedList,
                        meetingsForSelectedDate = meetingsForDate,
                        currentUserId = userId,
                        selectedDate = targetDate,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    fun openMeetingDetails(summaryMeeting: Meeting) {
        _uiState.update { currentState ->
            if (currentState is ScheduleUiState.Success) {
                currentState.copy(
                    selectedMeeting = summaryMeeting,
                    isMeetingDetailsLoading = false
                )
            } else currentState
        }
    }

    fun closeMeetingDetails() {
        _uiState.update { currentState ->
            if (currentState is ScheduleUiState.Success) {
                currentState.copy(selectedMeeting = null, isMeetingDetailsLoading = false)
            } else currentState
        }
    }

    fun onEditMeetingClick() {
        _uiState.update { currentState ->
            if (currentState is ScheduleUiState.Success) {
                val meeting = currentState.selectedMeeting ?: return@update currentState
                currentState.copy(
                    selectedMeeting = null,
                    meetingToEdit = meeting
                )
            } else currentState
        }
    }

    fun closeEditDialog() {
        _uiState.update { currentState ->
            if (currentState is ScheduleUiState.Success) {
                currentState.copy(meetingToEdit = null)
            } else currentState
        }
    }

    fun updateMeeting(meetingId: String, title: String, description: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is ScheduleUiState.Success) {
                    currentState.copy(isLoading = true)
                } else currentState
            }

            val result = repository.updateMeeting(
                meetingId = meetingId,
                title = title,
                description = description
            )

            result.onSuccess { updatedMeeting ->
                _uiState.update { currentState ->
                    if (currentState is ScheduleUiState.Success) {
                        val updatedList = currentState.meetings.map {
                            if (it.id == updatedMeeting.id) updatedMeeting else it
                        }
                        val updatedFiltered = updatedList.filter { it.date == currentState.selectedDate }

                        currentState.copy(
                            meetings = updatedList,
                            meetingsForSelectedDate = updatedFiltered,
                            meetingToEdit = null,
                            isLoading = false
                        )
                    } else currentState
                }
            }.onFailure { error ->
                _uiState.update { currentState ->
                    if (currentState is ScheduleUiState.Success) {
                        currentState.copy(isLoading = false)
                    } else currentState
                }
            }
        }
    }
}