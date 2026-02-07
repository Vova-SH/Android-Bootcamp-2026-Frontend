package ru.sicampus.bootcamp2026.ui.screen.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import ru.sicampus.bootcamp2026.domain.GetMyMeetingsUseCase

class ScheduleStateModel(
    private val getMyMeetingsUseCase: GetMyMeetingsUseCase = GetMyMeetingsUseCase()
) : ViewModel() {
    private val _uiState: MutableStateFlow<ScheduleState> = MutableStateFlow(ScheduleState.Content())
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.emit(ScheduleState.Loading)
            getMyMeetingsUseCase().onSuccess { meetings ->
                _uiState.emit(ScheduleState.Content(meetings))
            }.onFailure { e ->
                _uiState.emit(ScheduleState.Error(e.message ?: "Ошибка при загрузке расписания"))
            }
        }
    }
}
