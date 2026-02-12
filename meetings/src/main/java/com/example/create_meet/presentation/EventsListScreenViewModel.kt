package com.example.create_meet.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.create_meet.data.InvitationResponse
import com.example.create_meet.domain.DomainResult
import com.example.create_meet.domain.use_cases.AcceptInvitationUseCase
import com.example.create_meet.domain.use_cases.DeclineInvitationUseCase
import com.example.create_meet.domain.use_cases.GetInvitationsUseCase
import com.example.create_meet.domain.use_cases.LoadMeetingsUseCase
import com.example.create_meet.domain.use_cases.ObserveMeetingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventsListScreenViewModel @Inject constructor(
    observeMeetings: ObserveMeetingsUseCase,
    private val loadMeetings: LoadMeetingsUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val declineInvitationUseCase: DeclineInvitationUseCase,
    private val getInvitationsUseCase: GetInvitationsUseCase,
    ) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _error = MutableStateFlow<Throwable?>(null)
    private val _actionState = MutableStateFlow<ActionState>(ActionState.Idle)
    val actionState: StateFlow<ActionState> = _actionState

    private val _invitations = MutableStateFlow<List<InvitationResponse>>(emptyList())
    val invitations: StateFlow<List<InvitationResponse>> = _invitations

    val uiState: StateFlow<EventsUiState> =
        observeMeetings()
            .combine(_error) { meetings, error ->
                when {
                    error != null ->
                        EventsUiState.Error(error.message ?: "Ошибка загрузки")

                    else ->
                        EventsUiState.Success(meetings)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EventsUiState.Loading
            )

    init {
        refresh()
        loadInvitations()
    }
    fun resetActionState() {
        _actionState.value = ActionState.Idle
    }
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null
            Log.d("Refresh","$_isRefreshing")

            try {
                loadMeetings()
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isRefreshing.value = false
            }
        }
    }
    fun loadInvitations() {
        viewModelScope.launch {
            _invitations.value = emptyList()
            _error.value = null

            val result = getInvitationsUseCase()
            result.onSuccess {
                _invitations.value = result.getOrNull().orEmpty()

            }.onFailure {
                _error.value = it

            }

        }
    }
    fun accept(meetingId: String) {
        viewModelScope.launch {
            _actionState.value = ActionState.Loading

            val invite = _invitations.value.firstOrNull { it.meetingId == meetingId }
            if (invite == null) {
                _actionState.value = ActionState.Error("Приглашение не найдено")
                return@launch
            }

            when (val result = acceptInvitationUseCase(invite.id)) {
                is DomainResult.Success -> {
                    _actionState.value = ActionState.Success
                    loadInvitations()
                }
                is DomainResult.Error -> {
                    _actionState.value = ActionState.Error(result.message)
                }
            }

        }
    }

    fun decline(meetingId: String) {
        viewModelScope.launch {
            _actionState.value = ActionState.Loading

            val invite = _invitations.value.firstOrNull { it.meetingId == meetingId }
            if (invite == null) {
                _actionState.value = ActionState.Error("Приглашение не найдено")
                return@launch
            }

            when (val result = declineInvitationUseCase(invite.id)) {
                is DomainResult.Success -> {
                    _actionState.value = ActionState.Success
                    loadInvitations()
                }
                is DomainResult.Error -> {
                    _actionState.value = ActionState.Error(result.message)
                }
            }

        }
    }


}

