package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.android.data.InvitationsRepository
import ru.sicampus.bootcamp2026.android.data.MeetingsRepository
import ru.sicampus.bootcamp2026.android.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.data.source.InvitationsNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.MeetingsNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.PersonsNetworkDataSource
import ru.sicampus.bootcamp2026.android.domain.notifications_invite.GetMyInvitesWithMeetingUseCase
import ru.sicampus.bootcamp2026.android.domain.meetings.GetMyMeetingsForMonthUseCase
import ru.sicampus.bootcamp2026.android.domain.meetings.GetMyMeetingsForWeekUseCase
import ru.sicampus.bootcamp2026.android.domain.meetings.GetMyMeetingsForDayUseCase
import java.time.LocalDate
import java.time.format.DateTimeParseException

class HomeViewModel : ViewModel() {

    private val userInfo = PersonsNetworkDataSource()

    private val repo = MeetingsRepository(MeetingsNetworkDataSource())
    private val getDayUseCase = GetMyMeetingsForDayUseCase(repo)
    private val getWeekUseCase = GetMyMeetingsForWeekUseCase(repo)
    private val getMonthUseCase = GetMyMeetingsForMonthUseCase(repo)
    private val pageSize = 5

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private val invitesRepo = InvitationsRepository(InvitationsNetworkDataSource())
    private val getInvites = GetMyInvitesWithMeetingUseCase(invitesRepo)

    fun loadFirstPage(date: LocalDate) {
        _state.value = _state.value.copy(
            selectedDate = date,
            meetings = emptyList(),
            page = 0,
            isLast = false,
            isLoading = true,
            isLoadingMore = false,
            error = null
        )

        viewModelScope.launch {
            val result = getDayUseCase(
                date = date.toString(),
                page = 0,
                size = pageSize
            )

            result.onSuccess { page ->
                _state.value = _state.value.copy(
                    meetings = page.content,
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
        if (s.isLast || s.isLoadingMore || s.isLoading) return

        _state.value = s.copy(isLoadingMore = true, error = null)

        viewModelScope.launch {
            val nextPage = s.page + 1

            val result = getDayUseCase(
                date = s.selectedDate.toString(),
                page = nextPage,
                size = pageSize
            )

            result.onSuccess { page ->
                _state.value = _state.value.copy(
                    meetings = s.meetings + page.content,
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

    fun loadWeek(start: LocalDate) {
        viewModelScope.launch {
            val result = getWeekUseCase(start = start.toString())

            result.onSuccess { list ->
                val map = list.mapNotNull { dto ->
                    try {
                        LocalDate.parse(dto.date) to dto.count
                    } catch (_: DateTimeParseException) {
                        null
                    }
                }.toMap()

                _state.value = _state.value.copy(
                    weekCountsByDate = map
                )
            }.onFailure {
                _state.value = _state.value.copy(error = it.message)
            }
        }
    }

    fun loadMonth(month: String) { // "2026-02"
        viewModelScope.launch {
            val result = getMonthUseCase(month = month)

            result.onSuccess { list ->
                val map = list.mapNotNull { dto ->
                    try {
                        LocalDate.parse(dto.date) to dto.count
                    } catch (_: DateTimeParseException) {
                        null
                    }
                }.toMap()

                _state.value = _state.value.copy(
                    monthCountsByDate = map
                )
            }.onFailure {
                _state.value = _state.value.copy(error = it.message)
            }
        }
    }

    private fun fetchOrganizerNamesFor(meetings: List<MeetingResponse>) {
        val existing = _state.value.organizerNames
        val needIds = meetings
            .map { it.organizerId }
            .distinct()
            .filter { it !in existing.keys }

        if (needIds.isEmpty()) return

        viewModelScope.launch {
            val updates = mutableMapOf<Long, String>()

            needIds.forEach { id ->
                userInfo.getPerson(id)
                    .onSuccess { person ->
                        updates[id] = person.fullName
                    }
            }

            if (updates.isNotEmpty()) {
                _state.value = _state.value.copy(
                    organizerNames = _state.value.organizerNames + updates
                )
            }
        }
    }

    fun refreshPendingInvitesBadge() {
        viewModelScope.launch {
            val result = getInvites(
                status = InvitationStatusDto.PENDING,
                page = 0,
                size = 1
            )

            result.onSuccess { page ->
                _state.value = _state.value.copy(
                    hasPendingInvites = page.content.isNotEmpty()
                )
            }.onFailure {
            }
        }
    }
}