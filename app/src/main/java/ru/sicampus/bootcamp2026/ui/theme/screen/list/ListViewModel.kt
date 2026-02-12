package ru.sicampus.bootcamp2026.ui.theme.screen.auth.ru.sicampus.bootcamp2026.ui.theme.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase
import ru.sicampus.bootcamp2026.domain.entities.PagingUserListEntity


class ListViewModel : ViewModel() {
    private val mutex = Mutex()
    private val actualResult: MutableList<ListState.Item> = mutableListOf()
    private val getUsersUseCase = GetUsersUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )
    private val _uiState: MutableStateFlow<ListState> = MutableStateFlow(ListState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun onIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.LoadMore -> {
                getData(offset = actualResult.size)
            }

            is ListIntent.Refresh -> {
                getData(
                    offset = if (actualResult.isEmpty()) 0 else actualResult.size - 1
                )
            }
        }
    }

    private fun getData(offset: Int = 0) {
        val isFirstPage = offset == 0
        viewModelScope.launch {
            _uiState.emit(
                if (isFirstPage) {
                    ListState.Loading
                } else {
                    mutex.withLock {
                        dropLastTemporaryItem()
                        actualResult.add(ListState.Item.Loading)
                        (_uiState.value as? ListState.Content)?.copy(
                            users = actualResult.toPersistentList()
                        ) ?: ListState.Loading
                    }
                }
            )

            getUsersUseCase.invoke(offset).fold(
                onSuccess = { data ->
                    addItemsToState(isFirstPage, data)
                },
                onFailure = { error ->
                    error.printStackTrace()
                    _uiState.emit(
                        when (val state = _uiState.value) {
                            is ListState.Content -> {
                                mutex.withLock {
                                    dropLastTemporaryItem()
                                    actualResult.add(ListState.Item.Error)
                                    state.copy(
                                        users = actualResult.toPersistentList()
                                    )
                                }
                            }

                            is ListState.Error,
                            ListState.Loading -> {
                                ListState.Error(error.message.orEmpty())
                            }
                        }
                    )
                }
            )
        }
    }

    private suspend fun addItemsToState(
        isFirstPage: Boolean,
        data: PagingUserListEntity,
    ) {
        mutex.withLock {
            if (isFirstPage) {
                actualResult.clear()
            } else {
                dropLastTemporaryItem()
            }
            actualResult.addAll(
                data.users.map { item -> ListState.Item.User(item) }
            )
            _uiState.emit(
                ListState.Content(
                    isLastPage = data.isLast,
                    users = actualResult.toPersistentList()
                )
            )
        }
    }

    private fun dropLastTemporaryItem() {
        when (actualResult.last()) {
            is ListState.Item.Error,
            is ListState.Item.Loading -> actualResult.removeAt(actualResult.lastIndex)
            is ListState.Item.User -> Unit
        }
    }
}