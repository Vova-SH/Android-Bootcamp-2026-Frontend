package ru.sicampus.bootcamp2026.ui.screen.add

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.data.TimeRepository
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.data.source.TimeInfoDataSource
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.add.CreateEventUseCase
import ru.sicampus.bootcamp2026.domain.add.GetTimeUseCase
import ru.sicampus.bootcamp2026.domain.add.GetUsersUseCase
import ru.sicampus.bootcamp2026.domain.add.entities.PagingUserListEntity
import kotlin.Int

class AddViewModel: ViewModel() {
    private val mutex = Mutex()
    private val actualResult : MutableList<AddState.Item> = mutableListOf()

    private val createEventUseCase = CreateEventUseCase(
        eventRepository = EventRepository(EventInfoDataSource())
    )
    private val getTimeUseCase = GetTimeUseCase(
        timeRepository = TimeRepository(TimeInfoDataSource())
    )
    private val getUsersUseCase = GetUsersUseCase(
        userRepository = UserRepository( AuthNetworkDataSource(), UserInfoDataSource())
    )
    private val _uiState: MutableStateFlow<AddState> = MutableStateFlow(AddState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _timeSlotsState: MutableStateFlow<SlotsState> = MutableStateFlow(SlotsState.Loading)
    val timeSlotsState = _timeSlotsState.asStateFlow()

    private val _createState: MutableStateFlow<CreateState> = MutableStateFlow(CreateState.Loading)
    val createState = _createState.asStateFlow()

    init {
        getData()
    }
    fun onIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.LoadMore -> {
                getData(offset = actualResult.size)
            }

            is AddIntent.Refresh -> {
                getData(
                    // Проверяем с начала надо загрузить или с вычетом элемента ошибки
                    offset = if (actualResult.isEmpty()) 0 else actualResult.size - 1
                )
            }
        }
    }

    fun getTime(data: String){
        viewModelScope.launch {
            _timeSlotsState.emit(SlotsState.Loading)
            getTimeUseCase.invoke(data).fold(
                onSuccess = { data ->
                    _timeSlotsState.emit(SlotsState.Content(data))
                },
                onFailure = { error ->
                    _timeSlotsState.emit(SlotsState.Error(error.message.orEmpty()))
                }
            )
        }
    }

    fun createEvent(
        organizerId: Int,
        title: TextFieldValue,
        description: TextFieldValue,
        date: String,
        startTime: String,
        endTime: String,
        participantsId: List<Int>
    ){
        viewModelScope.launch {
            _createState.emit(CreateState.Loading)
            createEventUseCase.invoke(organizerId, title.toString(), description.toString(), date, startTime, endTime, participantsId).fold(
                onSuccess = { user ->
                    _createState.emit(CreateState.Content(user))
                },
                onFailure = { error ->
                    _createState.emit(CreateState.Error(error.message.orEmpty()))
                }
            )
        }
    }

    private fun getData(offset: Int = 0) {
        val isFirstPage = offset == 0
        viewModelScope.launch {
            // В начале определяем где нарисовать "крутилку"
            _uiState.emit(
                if (isFirstPage) {
                    AddState.Loading
                } else {
                    mutex.withLock {
                        dropLastTemporaryItem()
                        actualResult.add(AddState.Item.Loading)
                        (_uiState.value as? AddState.Content)?.copy(
                            users = actualResult.toPersistentList()
                        ) ?: AddState.Loading
                    }
                }
            )

            // Запрашиваем данные
            getUsersUseCase.invoke(offset).fold(
                onSuccess = { data ->
                    addItemsToState(isFirstPage, data)
                },
                onFailure = { error ->
                    error.printStackTrace()
                    _uiState.emit(
                        when (val state = _uiState.value) {
                            is AddState.Content -> {
                                mutex.withLock {
                                    dropLastTemporaryItem()
                                    actualResult.add(AddState.Item.Error)
                                    state.copy(
                                        users = actualResult.toPersistentList()
                                    )
                                }
                            }

                            is AddState.Error,
                            AddState.Loading -> {
                                AddState.Error(error.message.orEmpty())
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
                data.users.map { item -> AddState.Item.User(item) }
            )
            _uiState.emit(
                AddState.Content(
                    isLastPage = data.isLast,
                    users = actualResult.toPersistentList()
                )
            )
        }
    }

    private fun dropLastTemporaryItem() {
        when (actualResult.last()) {
            is AddState.Item.Error,
            is AddState.Item.Loading -> actualResult.removeAt(actualResult.lastIndex)
            is AddState.Item.User -> Unit
        }
    }
}