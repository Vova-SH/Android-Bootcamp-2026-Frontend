package ru.sicampus.bootcamp2026.ui.screens.invites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.data.source.MeetingInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetMeetingsUseCase

class InvitesViewModel(): ViewModel() {
    private val getMeetingsUseCase = GetMeetingsUseCase(
        meetingRepository = MeetingRepository(MeetingInfoDataSource())
    )

    private val _uiState = MutableStateFlow<InvitesState>(InvitesState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.emit(InvitesState.Loading)
            getMeetingsUseCase.invoke().fold(
                onSuccess = { meeting ->
                    _uiState.emit(InvitesState.Content(meeting))
                },
                onFailure = { error ->
                    _uiState.emit(InvitesState.Error(error.message.orEmpty()))
                }
            )
            _uiState.emit(InvitesState.Error("Ошибка получения встреч"))
        }
    }

}