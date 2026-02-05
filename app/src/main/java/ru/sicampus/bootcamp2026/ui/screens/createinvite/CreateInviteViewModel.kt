package ru.sicampus.bootcamp2026.ui.screens.createinvite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.data.source.MeetingInfoDataSource
import ru.sicampus.bootcamp2026.domain.CreateMeetingUseCase

class CreateInviteViewModel(): ViewModel() {
    private val createMeetingUseCase = CreateMeetingUseCase(
        meetingRepository = MeetingRepository(MeetingInfoDataSource())
    )
    private val _uiState = MutableStateFlow<CreateInviteState>(CreateInviteState.Loading)
    val uiState = _uiState.asStateFlow()

    fun createData(
        name: String?,
        startTime: Byte?,
        endTime: Byte?,
        date: String?,
        participants: List<Int>?
    ) {
        viewModelScope.launch {
            _uiState.emit(CreateInviteState.Loading)
            createMeetingUseCase.invoke(name, startTime, endTime, date, participants).fold(
                onSuccess = {

                },
                onFailure = { error ->
                    _uiState.emit(CreateInviteState.Error(error.message.orEmpty()))
                }
            )
            _uiState.emit(CreateInviteState.Error("Ошибка обновления данных"))

        }
    }
}