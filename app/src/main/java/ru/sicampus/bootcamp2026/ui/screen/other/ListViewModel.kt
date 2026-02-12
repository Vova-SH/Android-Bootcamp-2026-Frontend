package ru.sicampus.bootcamp2026.ui.screen.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.network.UserRepository
import ru.sicampus.bootcamp2026.data.network.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase

class ListViewModel: ViewModel() {
    private val getUsersUseCase = GetUsersUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )
    private val _uiState : MutableStateFlow<ListState> = MutableStateFlow(ListState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.emit(ListState.Loading)
            getUsersUseCase.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(ListState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(ListState.Error(error.message.orEmpty()))
                }
            )
        }
    }
}

