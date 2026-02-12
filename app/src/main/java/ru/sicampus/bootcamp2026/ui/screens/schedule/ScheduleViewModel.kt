package ru.sicampus.bootcamp2026.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.entities.MeetingMini
import ru.sicampus.bootcamp2026.domain.usecase.ViewScheduleUseCase
import ru.sicampus.bootcamp2026.domain.usecase.meeting.GetDayScheduleUseCase
import ru.sicampus.bootcamp2026.ui.screens.profile.ActionState
import ru.sicampus.bootcamp2026.ui.screens.profile.ProfileState
import ru.sicampus.bootcamp2026.ui.screens.profile.UserProfileUiData
import java.time.LocalDate
import java.time.temporal.IsoFields

enum class Period {
    DAY, WEEK, MONTH
}

class ScheduleViewModel(
    private val getDayScheduleUseCase: GetDayScheduleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScheduleState>(ScheduleState.Loading)
    val uiState: StateFlow<ScheduleState> = _uiState.asStateFlow()

    private val _state = MutableStateFlow(ScheduleUiState())
    val state: StateFlow<ScheduleUiState> = _state.asStateFlow()

    private val _navigationEvents: Channel<ActionState> = Channel()
    val navigationEvents: Flow<ActionState> = _navigationEvents.receiveAsFlow()


    init {
        loadSchedule()
    }

    fun selectPeriod(period: Period) {
        _state.update { it.copy(selectedPeriod = period) }
        loadSchedule()
    }

    fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
        loadSchedule()
    }

    fun refresh() {
        loadSchedule()
    }

    private fun loadSchedule() {
        _uiState.update { ScheduleState.Loading }

        viewModelScope.launch {
            when (_state.value.selectedPeriod) {
                Period.DAY -> loadDaySchedule()
                Period.WEEK -> loadWeekSchedule()
                Period.MONTH -> loadMonthSchedule()
            }
        }
    }

    private suspend fun loadDaySchedule() {
        val result = getDayScheduleUseCase.invoke(_state.value.selectedDate)

        result.onSuccess { meetingMinis ->
            _state.update { it.copy(dayMeetings = meetingMinis) }
            _uiState.update { ScheduleState.DayData }
        }.onFailure { error ->
            _state.update { state ->
                state.copy(
                    errorMessage = error.message ?: "Ошибка загрузки расписания"
                )
            }
            _uiState.update { ScheduleState.ErrorData }
        }
    }

    private suspend fun loadWeekSchedule() {
//        val year = _state.value.selectedDate.year
//        val week = _state.value.selectedDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
//        val result = viewScheduleUseCase.getWeekSchedule(year, week)
//        result.fold(
//            onSuccess = { meetings ->
//                _state.update { it.copy(weekMeetings = meetings) }
//            },
//            onFailure = { error ->
//                throw error
//            }
//        )
    }

    private suspend fun loadMonthSchedule() {
//        val year = _state.value.selectedDate.year
//        val month = _state.value.selectedDate.monthValue
//        val result = viewScheduleUseCase.getMonthSchedule(year, month)
//        result.fold(
//            onSuccess = { meetings ->
//                _state.update { it.copy(monthMeetings = meetings) }
//            },
//            onFailure = { error ->
//                throw error
//            }
//        )
    }

//    fun clearError() {
//        _state.update { it.copy(error = null) }
//    }

    fun getMeetingsForDate(date: LocalDate): List<MeetingMini> {
        return when (_state.value.selectedPeriod) {
            Period.DAY -> if (date == _state.value.selectedDate) _state.value.dayMeetings else emptyList()
            Period.WEEK -> _state.value.weekMeetings[date] ?: emptyList()
            Period.MONTH -> _state.value.monthMeetings[date] ?: emptyList()
        }
    }

    fun hasMeetingsForDate(date: LocalDate): Boolean {
        return when (_state.value.selectedPeriod) {
            Period.DAY -> date == _state.value.selectedDate && _state.value.dayMeetings.isNotEmpty()
            Period.WEEK -> _state.value.weekMeetings[date]?.isNotEmpty() ?: false
            Period.MONTH -> _state.value.monthMeetings[date]?.isNotEmpty() ?: false
        }
    }
}