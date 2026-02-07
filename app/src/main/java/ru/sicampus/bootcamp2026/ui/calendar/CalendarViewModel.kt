package ru.sicampus.bootcamp2026.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel для экрана календаря
 */
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMeetings()
    }

    fun onEvent(event: CalendarUiEvent) {
        when (event) {
            is CalendarUiEvent.LoadMeetings -> loadMeetings()
            is CalendarUiEvent.RefreshMeetings -> refreshMeetings()
            is CalendarUiEvent.SelectDate -> selectDate(event.date)
            is CalendarUiEvent.ChangeMonth -> changeMonth(event.month)
            is CalendarUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = meetingRepository.getUserMeetings(
                status = null,
                page = 0,
                size = 100
            )

            when (result) {
                is Result.Success -> {
                    val meetings = result.data.content
                    val state = _uiState.value
                    _uiState.update {
                        it.copy(
                            meetings = meetings,
                            meetingsForSelectedDate = filterMeetingsByDate(meetings, state.selectedDate),
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to load meetings: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun refreshMeetings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            val result = meetingRepository.getUserMeetings(
                status = null,
                page = 0,
                size = 100
            )

            when (result) {
                is Result.Success -> {
                    val meetings = result.data.content
                    val state = _uiState.value
                    _uiState.update {
                        it.copy(
                            meetings = meetings,
                            meetingsForSelectedDate = filterMeetingsByDate(meetings, state.selectedDate),
                            isRefreshing = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to refresh: ${result.exception.message}",
                            isRefreshing = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        val state = _uiState.value
        _uiState.update {
            it.copy(
                selectedDate = date,
                meetingsForSelectedDate = filterMeetingsByDate(state.meetings, date)
            )
        }
    }

    private fun changeMonth(month: LocalDate) {
        _uiState.update {
            it.copy(currentMonth = month)
        }
    }

    private fun filterMeetingsByDate(meetings: List<Meeting>, date: LocalDate): List<Meeting> {
        return meetings.filter { meeting ->
            meeting.startTime.toLocalDate() == date
        }.sortedBy { it.startTime }
    }
}

