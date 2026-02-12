package com.example.planify.main.navigation.screens.inbox_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.features.actions.domain.entities.Action
import com.example.planify.main.features.actions.domain.services.ActionsService
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.features.meetings.domain.schemas.actions.UserActionInvitedToMeetingSchema
import com.example.planify.main.features.meetings.domain.services.MeetingInvitesService
import com.example.planify.main.features.meetings.domain.services.MeetingsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val actionService: ActionsService,
    private val meetingsService: MeetingsService,
    private val meetingInvitesService: MeetingInvitesService
): ViewModel() {

    private val _uiState = MutableStateFlow(InboxState.empty())
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableStateFlow<List<Action<*>>>(emptyList())
    val actions = _actions.asStateFlow()

    init {
        viewModelScope.launch {
            actionService.actionsFlow.collect { action ->
                _actions.update { it + action }
                Log.d("ACTION", action.type)
                when(action.type) {
                    "meetings:invited" -> {
                        processMeetingInvite(action as Action<UserActionInvitedToMeetingSchema>)
                    }
                    "meetings:invite_reschedule_requested" -> {

                    }
                }
            }
        }
    }

    suspend fun processMeetingInvite(action: Action<UserActionInvitedToMeetingSchema>) {
        _uiState.update { it.copy(actions = it.actions.plus(
            action.id to ResourceState.Loading
        )) }

        fetchMeetingContext(action.data!!.meetingId)
            .onSuccess { context ->
                _uiState.update { it.copy(actions = it.actions.plus(
                    action.id to ResourceState.Success(InboxAction.Invite(
                        meetingContext = context,
                        inviteUuid = action.data.inviteUuid,
                        actionId = action.id
                    ))
                )) }
            }
    }

    suspend fun fetchMeetingContext(meetingId: Long): Result<MeetingContext> {
        return meetingsService.fetchMeetingContext(meetingId)
    }

    fun acceptMeeting(inviteUuid: String, actionId: String) {
        viewModelScope.launch {
            meetingInvitesService.inviteAccept(inviteUuid, actionId)
            _uiState.update { it.copy(actions = it.actions.minus(actionId)) }
        }
    }

    fun rejectMeeting(inviteUuid: String, actionId: String) {
        viewModelScope.launch {
            meetingInvitesService.inviteReject(inviteUuid, actionId)
            _uiState.update { it.copy(actions = it.actions.minus(actionId)) }
        }
    }
}
