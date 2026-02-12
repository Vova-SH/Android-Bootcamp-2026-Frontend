package ru.sicampus.bootcamp2026.ui.screens.users

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
import ru.sicampus.bootcamp2026.domain.users.GetUsersUseCase
import ru.sicampus.bootcamp2026.domain.users.entities.PagingUserListEntity

class UsersViewModel: ViewModel() {
    private val mutex = Mutex()
    private val actualResult: MutableList<UsersState.Item> = mutableListOf()
    private val getUsersUseCase = GetUsersUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )
    private val _uiState: MutableStateFlow<UsersState> = MutableStateFlow(UsersState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun onIntent(intent: UsersIntent) {
        when (intent) {
            is UsersIntent.LoadMore -> {
               getData(offset = actualResult.size)
            }
            is UsersIntent.Refresh -> {
                getData(offset = if (actualResult.isEmpty()) 0 else actualResult.size - 1)
            }
        }
    }

    private fun getData(offset: Int = 0) {
        val isFirstPage = offset == 0
        viewModelScope.launch {
            _uiState.emit(
                if(isFirstPage) {
                    UsersState.Loading
                } else {
                    mutex.withLock {
                        dropLastTemporaryItem()
                        actualResult.add(UsersState.Item.Loading)
                        (_uiState.value as? UsersState.Content)?.copy(
                            users = actualResult.toPersistentList()
                        ) ?: UsersState.Loading
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
                            is UsersState.Content -> {
                                mutex.withLock {
                                    dropLastTemporaryItem()
                                    actualResult.add(UsersState.Item.Error)
                                    state.copy(
                                        users = actualResult.toPersistentList()
                                    )
                                }
                            }
                            is UsersState.Error,
                            UsersState.Loading -> {
                                UsersState.Error(error.message.orEmpty())
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
                data.users.map { item -> UsersState.Item.User(item) }
            )
            _uiState.emit(
                UsersState.Content(
                    isLastPage = data.isLast,
                    users = actualResult.toPersistentList()
                )
            )
        }
    }
    private fun dropLastTemporaryItem() {
        when (actualResult.last()) {
            is UsersState.Item.Error,
            is UsersState.Item.Loading -> actualResult.removeAt(actualResult.lastIndex)
            is UsersState.Item.User -> Unit
        }
    }
}
