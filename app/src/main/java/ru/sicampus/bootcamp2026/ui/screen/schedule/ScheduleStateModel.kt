package ru.sicampus.bootcamp2026.ui.screen.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.update
import ru.sicampus.bootcamp2026.domain.GetMyMeetingsUseCase
import ru.sicampus.bootcamp2026.util.ErrorUtils
import java.time.LocalDate

class ScheduleStateModel(
    private val getMyMeetingsUseCase: GetMyMeetingsUseCase = GetMyMeetingsUseCase()
) : ViewModel() {
    private val _uiState = MutableStateFlow<ScheduleState>(ScheduleState.Content())
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun onDateSelected(date: LocalDate) {
        val currentState = _uiState.value
        if (currentState is ScheduleState.Content) {
            _uiState.update { currentState.copy(selectedDate = date) }
        }
    }

    fun getData() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val selectedDate = if (currentState is ScheduleState.Content) currentState.selectedDate else LocalDate.now()
            
            _uiState.emit(ScheduleState.Loading)
            getMyMeetingsUseCase().onSuccess { meetings ->
                _uiState.emit(ScheduleState.Content(meetings, selectedDate))
            }.onFailure { e ->
                _uiState.emit(ScheduleState.Error(ErrorUtils.translateError(e, "Ошибка при загрузке расписания")))
            }
        }
    }
}
