package ru.innovationcampus.android.ui.screen.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.innovationcampus.android.data.MeetingRepository
import ru.innovationcampus.android.data.source.MeetingInfoDataSource
import ru.innovationcampus.android.domain.list.entities.PagingUserListEntity
import ru.innovationcampus.android.domain.meetings.GetMeetingsUseCase
import ru.innovationcampus.android.domain.meetings.entities.PagingMeetingListEntity
import ru.innovationcampus.android.ui.screen.list.ListIntent
import ru.innovationcampus.android.ui.screen.list.ListState

class MeetingsListViewModel: ViewModel() {
    private val mutex = Mutex()
    private val actualResult: MutableList<MeetingsListState.Item> = mutableListOf()
    private val getMeetingsUseCase = GetMeetingsUseCase(
        meetingRepository = MeetingRepository(MeetingInfoDataSource())
    )
    private val _uiState: MutableStateFlow<MeetingsListState> = MutableStateFlow(MeetingsListState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun onIntent(intent: MeetingsListIntent) {
        when (intent) {
            is MeetingsListIntent.LoadMore -> {
                getData(offset = actualResult.size)
            }

            is MeetingsListIntent.Refresh -> {
                getData(
                    // Проверяем с начала надо загрузить или с вычетом элемента ошибки
                    offset = if (actualResult.isEmpty()) 0 else actualResult.size - 1
                )
            }
        }
    }

    private fun getData(offset: Int = 0) {
        val isFirstPage = offset == 0
        viewModelScope.launch {
            // В начале определяем где нарисовать "крутилку"
            _uiState.emit(
                if (isFirstPage) {
                    MeetingsListState.Loading
                } else {
                    mutex.withLock {
                        dropLastTemporaryItem()
                        actualResult.add(MeetingsListState.Item.Loading)
                        (_uiState.value as? MeetingsListState.Content)?.copy(
                            meetings = actualResult.toPersistentList()
                        ) ?: MeetingsListState.Loading
                    }
                }
            )

            // Запрашиваем данные
            getMeetingsUseCase.invoke(offset).fold(
                onSuccess = { data ->
                    addItemsToState(isFirstPage, data)
                },
                onFailure = { error ->
                    error.printStackTrace()
                    _uiState.emit(
                        when (val state = _uiState.value) {
                            is MeetingsListState.Content -> {
                                mutex.withLock {
                                    dropLastTemporaryItem()
                                    actualResult.add(MeetingsListState.Item.Error)
                                    state.copy(
                                        meetings = actualResult.toPersistentList()
                                    )
                                }
                            }

                            is MeetingsListState.Error,
                            MeetingsListState.Loading -> {
                                MeetingsListState.Error(error.message.orEmpty())
                            }
                            else -> {MeetingsListState.Error("test")}
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
                data.meetings.map { item -> MeetingsListState.Item.Meeting(item) }
            )
            _uiState.emit(
                MeetingsListState.Content(
                    isLastPage = data.isLast,
                    meetings = actualResult.toPersistentList()
                )
            )
        }
    }

    private fun dropLastTemporaryItem() {
        when (actualResult.last()) {
            is MeetingsListState.Item.Error,
            is MeetingsListState.Item.Loading -> actualResult.removeAt(actualResult.lastIndex)
            is MeetingsListState.Item.Meeting -> Unit
        }
    }
}