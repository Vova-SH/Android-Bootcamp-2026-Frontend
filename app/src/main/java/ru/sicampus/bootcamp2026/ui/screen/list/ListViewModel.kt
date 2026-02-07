package ru.sicampus.bootcamp2026.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.domain.list.ChangeInvitationsUseCase
import ru.sicampus.bootcamp2026.domain.list.GetInvitationsUseCase


class ListViewModel : ViewModel() {

    private val getInvitationsUseCase = GetInvitationsUseCase(
        eventRepository = EventRepository(EventInfoDataSource())
    )
    private val changeInvitationsUseCase = ChangeInvitationsUseCase(
        eventRepository = EventRepository(EventInfoDataSource())
    )
    private val _uiState : MutableStateFlow<ListState> =  MutableStateFlow(ListState.Loading)
    val uiState: StateFlow<ListState> = _uiState.asStateFlow()

    init {
        getData()
    }
    fun getData(){
        viewModelScope.launch {
            _uiState.emit(ListState.Loading)
            getInvitationsUseCase.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(ListState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(ListState.Error(error.message.orEmpty()))
                }
            )
        }
    }

    fun onIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.Send -> {
                println("ListViewModel: Send intent with response: ${intent.response}")
                viewModelScope.launch {
                    changeInvitationsUseCase.invoke(intent.meetingId, intent.userId, intent.response).fold(
                        onSuccess = {
                            println("ListViewModel: Change successful")
                            getData()
                        },
                        onFailure = { error ->
                            _uiState.emit(ListState.Error(error.message.orEmpty()))
                        }
                    )
                }
            }
        }
    }
}