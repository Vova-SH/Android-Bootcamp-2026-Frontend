//package ru.sicampus.bootcamp2026.ui.screens.schedule
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto
//import ru.sicampus.bootcamp2026.domain.usecase.ViewScheduleUseCase
//import java.time.LocalDate
//
//data class ScheduleState(
//    val selectedPeriod: Period = Period.DAY,
//    val selectedDate: LocalDate = LocalDate.now(),
//    val meetings: List<MeetingMiniDto> = emptyList(),
//    val isLoading: Boolean = false,
//    val error: String? = null
//)
//
//enum class Period {
//    DAY, WEEK, MONTH
//}
//
//class ScheduleViewModel(
//    private val viewScheduleUseCase: ViewScheduleUseCase
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(ScheduleState())
//    val state: StateFlow<ScheduleState> = _state.asStateFlow()
//
//    init {
//        loadSchedule()
//    }
//
//    fun selectPeriod(period: Period) {
//        _state.value = _state.value.copy(selectedPeriod = period)
//        loadSchedule()
//    }
//
//    fun selectDate(date: LocalDate) {
//        _state.value = _state.value.copy(selectedDate = date)
//        loadSchedule()
//    }
//
//    fun loadSchedule() {
//        viewModelScope.launch {
//            _state.value = _state.value.copy(isLoading = true, error = null)
//
//            val result = when (_state.value.selectedPeriod) {
//                Period.DAY -> viewScheduleUseCase.getDaySchedule(_state.value.selectedDate)
//                Period.WEEK -> {
//                    val year = _state.value.selectedDate.year
//                    val week = _state.value.selectedDate.get(java.time.temporal.WeekFields.ISO.weekOfYear())
//                    viewScheduleUseCase.getWeekSchedule(year, week)
//                }
//                Period.MONTH -> {
//                    val year = _state.value.selectedDate.year
//                    val month = _state.value.selectedDate.monthValue
//                    viewScheduleUseCase.getMonthSchedule(year, month)
//                }
//            }
//
//            result.fold(
//                onSuccess = { meetings ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        meetings = meetings
//                    )
//                },
//                onFailure = { error ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = error.message ?: "Ошибка загрузки расписания",
//                        meetings = emptyList()
//                    )
//                }
//            )
//        }
//    }
//
//    fun clearError() {
//        _state.value = _state.value.copy(error = null)
//    }
//}