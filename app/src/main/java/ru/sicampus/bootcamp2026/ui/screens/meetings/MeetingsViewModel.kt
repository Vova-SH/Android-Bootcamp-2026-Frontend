package ru.sicampus.bootcamp2026.ui.screens.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.MeetingInfoDataSource
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.users.GetMeetingsUseCase
import ru.sicampus.bootcamp2026.domain.users.GetPlannedMeetingsUseCase
import ru.sicampus.bootcamp2026.domain.users.GetUsersUseCase
import ru.sicampus.bootcamp2026.domain.users.entities.PagingMeetingListEntity
import ru.sicampus.bootcamp2026.domain.users.entities.PagingUserListEntity

class MeetingsViewModel: ViewModel() {
    private val mutex = Mutex()
    private val actualResult: MutableList<MeetingsState.Item> = mutableListOf()
    private val getMeetingsUseCase = GetMeetingsUseCase(
        meetingRepository = MeetingRepository(MeetingInfoDataSource())
    )
    private val _uiState: MutableStateFlow<MeetingsState> = MutableStateFlow(MeetingsState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun onIntent(intent: MeetingsIntent) {
        when (intent) {
            is MeetingsIntent.LoadMore -> {
               getData(offset = actualResult.size)
            }
            is MeetingsIntent.Refresh -> {
                getData(offset = if (actualResult.isEmpty()) 0 else actualResult.size - 1)
            }
        }
    }

    private fun getData(offset: Int = 0) {
        val isFirstPage = offset == 0
        viewModelScope.launch {
            _uiState.emit(
                if(isFirstPage) {
                    MeetingsState.Loading
                } else {
                    mutex.withLock {
                        dropLastTemporaryItem()
                        actualResult.add(MeetingsState.Item.Loading)
                        (_uiState.value as? MeetingsState.Content)?.copy(
                            meetings = actualResult.toPersistentList()
                        ) ?: MeetingsState.Loading
                    }
                }
            )

            getMeetingsUseCase.invoke(offset).fold(
                onSuccess = { data ->
                    addItemsToState(isFirstPage, data)
                },
                onFailure = { error ->
                    error.printStackTrace()
                    _uiState.emit(
                        when (val state = _uiState.value) {
                            is MeetingsState.Content -> {
                                mutex.withLock {
                                    dropLastTemporaryItem()
                                    actualResult.add(MeetingsState.Item.Error)
                                    state.copy(
                                        meetings = actualResult.toPersistentList()
                                    )
                                }
                            }
                            is MeetingsState.Error,
                            MeetingsState.Loading -> {
                                MeetingsState.Error(error.message.orEmpty())
                            }
                        }
                    )
                }
            )
        }
    }

    private suspend fun addItemsToState(
        isFirstPage: Boolean,
        data: PagingMeetingListEntity,
    ) {
        mutex.withLock {
            if (isFirstPage) {
                actualResult.clear()
            } else {
                dropLastTemporaryItem()
            }
            actualResult.addAll(
                data.meetings.map { item -> MeetingsState.Item.Meeting(item) }
            )
            _uiState.emit(
                MeetingsState.Content(
                    isLastPage = data.isLast,
                    meetings = actualResult.toPersistentList()
                )
            )
        }
    }
    private fun dropLastTemporaryItem() {
        when (actualResult.last()) {
            is MeetingsState.Item.Error,
            is MeetingsState.Item.Loading -> actualResult.removeAt(actualResult.lastIndex)
            is MeetingsState.Item.Meeting -> Unit
        }
    }
}