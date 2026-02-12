package ru.sicampus.bootcamp2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.auth.NetworkClient
import ru.sicampus.bootcamp2026.data.auth.TokenStorage
import ru.sicampus.bootcamp2026.data.model.MeetingDto
import ru.sicampus.bootcamp2026.data.repository.AppRepository

class InvitesViewModel(app: Application) : AndroidViewModel(app) {
    private val tokenStorage = TokenStorage(app)
    private val api = NetworkClient.createAppApi()
    private val repository = AppRepository(api, tokenStorage)

    private val _invites = MutableStateFlow<List<MeetingDto>>(emptyList())
    val invites = _invites.asStateFlow()

    init {
        loadInvites()

        val stomp = NetworkClient.getStompManager(tokenStorage)

        viewModelScope.launch {
            stomp.updates.collect {
                loadInvites()
            }
        }
    }

    private fun loadInvites() {
        viewModelScope.launch {
            val me = repository.getCurrentUser().getOrNull() ?: return@launch
            repository.getInvites(me.id, 0)
                .onSuccess { pageResponse -> _invites.value = pageResponse.content }
        }
    }

    fun answer(meetingId: Long, accept: Boolean) {
        viewModelScope.launch {
            val me = repository.getCurrentUser().getOrNull() ?: return@launch
            repository.answerInvite(me.id, meetingId, accept)
                .onSuccess {
                    _invites.value = _invites.value.filter { it.id != meetingId }
                }
        }
    }
}