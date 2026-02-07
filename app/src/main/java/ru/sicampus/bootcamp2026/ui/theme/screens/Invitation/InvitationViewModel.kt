package ru.sicampus.bootcamp2026.ui.theme.screens.Invitation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.ui.theme.screens.Profile.ProfileState

class InvitationViewModel(private val appViewModel: AppViewModel): ViewModel() {

    private val _uiState: MutableStateFlow<InvitationState> = MutableStateFlow(InvitationState.Meetings)

    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun toProfile() {
        appViewModel.NavigateTo(ViewModelState.Profile)
    }

    fun toTimeTable() {
        appViewModel.NavigateTo(ViewModelState.TimeTable)
    }

    fun toMeetInfo() {
        appViewModel.NavigateTo(ViewModelState.MeetingResponse)
    }



    fun getData(){
        viewModelScope.launch {
            _uiState.emit(InvitationState.Meetings)
            delay(2000L)
        }
    }
}