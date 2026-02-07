package ru.sicampus.bootcamp2026

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel: ViewModel() {
    private val _appState: MutableStateFlow<ViewModelState> = MutableStateFlow(ViewModelState.Loading)

    var appState = _appState.asStateFlow()

    init{
        getData()
    }

    fun NavigateTo(state: ViewModelState){
        _appState.value = state
    }

    fun getData() {
        viewModelScope.launch {
            _appState.emit(ViewModelState.Login)

            delay(2000L)

            //_appState.emit(ViewModelState.Error("Error"))
        }
    }
}