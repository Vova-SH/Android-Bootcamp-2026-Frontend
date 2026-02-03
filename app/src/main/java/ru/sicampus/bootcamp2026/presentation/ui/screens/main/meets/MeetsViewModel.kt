package ru.sicampus.bootcamp2026.presentation.ui.screens.main.meets

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.sicampus.bootcamp2026.presentation.ui.screens.main.create.users.list.ListState

class MeetsViewModel : ViewModel() {
    private val _state: MutableStateFlow<ListState> = MutableStateFlow<ListState>(ListState.Loading)
    val state = _state.asStateFlow()
}