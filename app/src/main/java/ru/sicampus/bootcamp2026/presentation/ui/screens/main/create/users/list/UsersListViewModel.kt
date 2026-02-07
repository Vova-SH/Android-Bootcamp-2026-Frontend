package ru.sicampus.bootcamp2026.presentation.ui.screens.main.create.users.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyListViewModel : ViewModel() {
    private val _state: MutableStateFlow<ListState> = MutableStateFlow<ListState>(ListState.Loading)
    val state = _state.asStateFlow()


    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _state.emit(ListState.Loading)
            delay(2000L) // todo
            _state.emit(ListState.Error("Mock Error"))
        }
    }
}
