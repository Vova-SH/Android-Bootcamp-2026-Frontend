package ru.sicampus.bootcamp2026.ui.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.domain.home.GetEventsUseCase
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.ui.screen.home.HomeState

class CalendarViewModel: ViewModel() {
    private val getEventsUseCase = GetEventsUseCase(
        eventRepository = EventRepository(EventInfoDataSource())
    )
    private val _uiState: MutableStateFlow<CalendarState> = MutableStateFlow(CalendarState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }
    fun getData(){
        viewModelScope.launch {
            _uiState.emit(CalendarState.Loading)
            getEventsUseCase.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(CalendarState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(CalendarState.Error(error.message.orEmpty()))
                }
            )
        }
    }
}