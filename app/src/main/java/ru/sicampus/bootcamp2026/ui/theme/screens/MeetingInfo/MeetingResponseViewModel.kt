package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase

class MeetingResponseViewModel(private val appViewModel: AppViewModel): ViewModel() {
    private val _uiState: MutableStateFlow<MeetingResponseState> = MutableStateFlow(MeetingResponseState.Loading)

    val uiState = _uiState.asStateFlow()
    val getUsersUseCase = GetUsersUseCase(UserRepository(UsersInfoDataSource()))
    init {
        getData()
    }

    fun closeInfo() {
        appViewModel.NavigateTo(ViewModelState.Invitations)
    }


    fun getData(){
        viewModelScope.launch {
            _uiState.emit(MeetingResponseState.Loading)
            getUsersUseCase.invoke(0).fold(
                onSuccess = {data ->
                    _uiState.emit(MeetingResponseState.Content(users = data))
                },
                onFailure = {error ->
                    _uiState.emit(MeetingResponseState.Error(error.message.orEmpty()))
                }

            )
        }
    }
}