package ru.sicampus.bootcamp2026.ui.theme.screens.CreateNewMeeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase

class CreateNewMeetingViewModel: ViewModel() {

    private val getUsersUseCase = GetUsersUseCase(
        UserRepository = UserRepository(UsersInfoDataSource())
    )
    private val _uiState: MutableStateFlow<CreateNewMeetingState> = MutableStateFlow(CreateNewMeetingState.Loading)
    val uiState = _uiState.asStateFlow()

    init{
        getData()
    }
    fun getData(){
        viewModelScope.launch {
            _uiState.emit(CreateNewMeetingState.Loading)

            delay(2000L)

            _uiState.emit(CreateNewMeetingState.Error("User error"))


        }
    }
}