package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.android.data.MeetingsRepository
import ru.sicampus.bootcamp2026.android.data.PersonsRepository
import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.data.source.MeetingsNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.PersonsNetworkDataSource
import ru.sicampus.bootcamp2026.android.domain.meetings.GetMyMeetingsForDayUseCase
import ru.sicampus.bootcamp2026.android.domain.meetings.GetMyMeetingsForWeekUseCase
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    private val repo = MeetingsRepository(MeetingsNetworkDataSource())
    private val getDayUseCase = GetMyMeetingsForDayUseCase(repo)

    private val getWeekUseCase = GetMyMeetingsForWeekUseCase(repo)

    private val pageSize = 5

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    fun loadFirstPage(date: LocalDate) {
        _state.value = HomeUiState(
            selectedDate = date,
            isLoading = true
        )

        viewModelScope.launch {

            val result = getDayUseCase(
                date = date.toString(),
                page = 0,
                size = pageSize
            )

            result.onSuccess { page ->

                _state.value = HomeUiState(
                    selectedDate = date,
                    meetings = page.content,
                    page = page.number,
                    isLast = page.last,
                    isLoading = false
                )
                ensureOrganizerNamesLoaded(page.content)
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

        if (s.isLast || s.isLoadingMore) return

        viewModelScope.launch {

            _state.value = s.copy(isLoadingMore = true)

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
                ensureOrganizerNamesLoaded(page.content)
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

            val result = getWeekUseCase(start.toString())

            result.onSuccess { list ->

                val map = list.associate {
                    LocalDate.parse(it.date) to it.count
                }

                _state.value = _state.value.copy(
                    weekCountsByDate = map
                )

            }.onFailure {
                _state.value = _state.value.copy(
                    error = it.message
                )
            }
        }
    }

    private val personsRepo = PersonsRepository(PersonsNetworkDataSource())

    private fun ensureOrganizerNamesLoaded(meetings: List<MeetingResponse>) {
        val existing = _state.value.organizerNames
        val needIds = meetings.map { it.organizerId }.distinct().filter { it !in existing.keys }

        if (needIds.isEmpty()) return

        viewModelScope.launch {
            val newMap = existing.toMutableMap()

            needIds.forEach { id ->
                personsRepo.getPerson(id).onSuccess { person ->
                    newMap[id] = person.fullName
                }
            }

            _state.value = _state.value.copy(organizerNames = newMap)
        }
    }
}
