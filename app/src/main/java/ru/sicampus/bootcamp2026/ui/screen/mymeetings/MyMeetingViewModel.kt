package ru.sicampus.bootcamp2026.ui.screen.mymeetings

import ru.sicampus.bootcamp2026.ui.screen.list.ListIntent


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.domain.mymeetings.DeleteMeetingUseCase
import ru.sicampus.bootcamp2026.domain.mymeetings.GetMeetingsUseCase


class MyMeetingViewModel : ViewModel() {

    private val getMeetingsUseCase = GetMeetingsUseCase(
        eventRepository = EventRepository(EventInfoDataSource())
    )
    private val deleteMeetUseCase = DeleteMeetingUseCase(
        eventRepository = EventRepository(EventInfoDataSource())
    )
    private val _uiState : MutableStateFlow<MyMeetingsState> =  MutableStateFlow(MyMeetingsState.Loading)
    val uiState: StateFlow<MyMeetingsState> = _uiState.asStateFlow()

    private val _selectedEvent = MutableStateFlow<EventEntity?>(null)
    val selectedEvent: StateFlow<EventEntity?> = _selectedEvent.asStateFlow()

    fun selectEvent(event: EventEntity) {
        _selectedEvent.value = event
    }

    init {
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            _uiState.emit(MyMeetingsState.Loading)
            getMeetingsUseCase.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(MyMeetingsState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(MyMeetingsState.Error(error.message.orEmpty()))
                }
            )
        }
    }

    fun onIntent(intent: MyMeetingIntent) {
        when (intent) {
            is MyMeetingIntent.Send -> {
                viewModelScope.launch {
                    deleteMeetUseCase.invoke(intent.meetingId).fold(
                        onSuccess = {
                            println("MyMeetingViewModel: Change successful")
                            getData()
                        },
                        onFailure = { error ->
                            _uiState.emit(MyMeetingsState.Error(error.message.orEmpty()))
                        }
                    )
                }
            }
        }
    }
}