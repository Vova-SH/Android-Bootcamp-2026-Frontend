package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.android.data.InvitationsRepository
import ru.sicampus.bootcamp2026.android.data.PersonsRepository
import ru.sicampus.bootcamp2026.android.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.android.data.dto.InvitationWithMeetingDto
import ru.sicampus.bootcamp2026.android.data.source.InvitationsNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.PersonsNetworkDataSource
import ru.sicampus.bootcamp2026.android.domain.notifications_invite.GetMyInvitesWithMeetingUseCase
import ru.sicampus.bootcamp2026.android.domain.notifications_invite.RespondInvitationUseCase

class NotificationsViewModel : ViewModel() {

    private val invitesRepo = InvitationsRepository(InvitationsNetworkDataSource())
    private val personsRepo = PersonsRepository(PersonsNetworkDataSource())
    private val getInvites = GetMyInvitesWithMeetingUseCase(invitesRepo)

    private val respondInvite = RespondInvitationUseCase(invitesRepo)

    private val pageSize = 10

    private val _state = MutableStateFlow(NotificationsUiState())
    val state = _state.asStateFlow()

    fun loadFirstPage() {
        _state.value = _state.value.copy(
            invites = emptyList(),
            page = 0,
            isLast = false,
            isLoading = true,
            isLoadingMore = false,
            error = null
        )

        viewModelScope.launch {
            val result = getInvites(
                status = InvitationStatusDto.PENDING,
                page = 0,
                size = pageSize
            )

            result.onSuccess { page ->
                _state.value = _state.value.copy(
                    invites = page.content,
                    page = page.number,
                    isLast = page.last,
                    isLoading = false
                )
                fetchOrganizerNamesFor(page.content)
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    fun loadNextPage() {
        val s = _state.value
        if (s.isLast || s.isLoading || s.isLoadingMore) return

        _state.value = s.copy(isLoadingMore = true, error = null)

        viewModelScope.launch {
            val nextPage = s.page + 1

            val result = getInvites(
                status = InvitationStatusDto.PENDING,
                page = nextPage,
                size = pageSize
            )

            result.onSuccess { page ->
                val newList = s.invites + page.content
                _state.value = _state.value.copy(
                    invites = newList,
                    page = page.number,
                    isLast = page.last,
                    isLoadingMore = false
                )
                fetchOrganizerNamesFor(page.content)
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoadingMore = false,
                    error = it.message
                )
            }
        }
    }

    private fun fetchOrganizerNamesFor(invites: List<InvitationWithMeetingDto>) {
        val existing = _state.value.organizerNames

        val needIds = invites
            .map { it.meeting.organizerId }
            .distinct()
            .filter { it !in existing.keys }

        if (needIds.isEmpty()) return

        viewModelScope.launch {
            val updates = mutableMapOf<Long, String>()

            needIds.forEach { id ->
                personsRepo.getPerson(id)
                    .onSuccess { person -> updates[id] = person.fullName }
            }

            if (updates.isNotEmpty()) {
                _state.value = _state.value.copy(
                    organizerNames = _state.value.organizerNames + updates
                )
            }
        }
    }

    fun respond(inviteId: Long, status: InvitationStatusDto) {
        viewModelScope.launch {
            val result = respondInvite(
                id = inviteId,
                status = status.name
            )
            result.onSuccess {
                _state.value = _state.value.copy(
                    invites = _state.value.invites
                        .filterNot { it.id == inviteId }
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    error = it.message
                )
            }
        }
    }
}