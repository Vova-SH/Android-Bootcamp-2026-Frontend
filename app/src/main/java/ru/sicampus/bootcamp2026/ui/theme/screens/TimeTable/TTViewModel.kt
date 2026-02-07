package ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState

class TTViewModel(private val appViewModel: AppViewModel): ViewModel() {

    private val _uiState: MutableStateFlow<TTState> = MutableStateFlow(TTState.Content)

    val uiState = _uiState.asStateFlow()

    init{
        getData()
    }

    fun toInvitations() {
        appViewModel.NavigateTo(ViewModelState.Invitations)
    }

    fun toProfile() {
        appViewModel.NavigateTo(ViewModelState.Profile)
    }

    fun SaveCreate() {
        viewModelScope.launch {
            _uiState.emit(TTState.Content)
        }
    }

    fun getData() {
        viewModelScope.launch {
            delay(2000L)
        }
    }
}