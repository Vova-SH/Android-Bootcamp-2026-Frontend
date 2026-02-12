package ru.sicampus.bootcamp2026.ui.screen.modal

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.sicampus.bootcamp2026.data.repository.EmployeeRepository
import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.data.source.EmployeeSearchDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingDataSource
import ru.sicampus.bootcamp2026.domain.CreateMeetingUseCase
import ru.sicampus.bootcamp2026.domain.SearchEmployeesUseCase
import ru.sicampus.bootcamp2026.domain.entities.CreateMeetingEntity
import ru.sicampus.bootcamp2026.domain.entities.PagingEmployeeListEntity
import ru.sicampus.bootcamp2026.ui.screen.meetings.EmployeeListIntent
import ru.sicampus.bootcamp2026.ui.screen.meetings.EmployeeSearchState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddMeetingViewModel : ViewModel() {
    private val mutex = Mutex()
    private val actualResult: MutableList<EmployeeSearchState.Item> = mutableListOf()

    private val searchEmployeesUseCase by lazy {
        SearchEmployeesUseCase(
            employeeRepository = EmployeeRepository(EmployeeSearchDataSource())
        )
    }
    private val createMeetingUseCase by lazy {
        CreateMeetingUseCase(
            repository = MeetingRepository(
                meetingDataSource = MeetingDataSource()
            )
        )
    }

    private val _uiState = MutableStateFlow<EmployeeSearchState>(EmployeeSearchState.Loading)
    val uiState = _uiState.asStateFlow()

    val titleState = TextFieldState()
    val locationState = TextFieldState()
    val dateState = TextFieldState()
    val timeState = TextFieldState()
    val searchState = TextFieldState()

    val isFormValid by derivedStateOf {
        titleState.text.isNotBlank() &&
                locationState.text.isNotBlank() &&
                dateState.text.isNotBlank() &&
                timeState.text.isNotBlank() &&
                _selectedUsernames.value.isNotEmpty()
    }

    private val _selectedUsernames = MutableStateFlow<Set<String>>(emptySet())
    val selectedUsernames = _selectedUsernames.asStateFlow()

    private val currentContentState: EmployeeSearchState.Content?
        get() = _uiState.value as? EmployeeSearchState.Content

    private val validationRegex = Regex("^[a-zA-Zа-яА-ЯёЁ\\s]*$")
    private var currentSearchQuery: String = ""

    init {
        observeSearch()
        loadInitialData()
    }

    private fun loadInitialData() {
        getData(query = "", offset = 0)
    }
    fun clearSelectedUsernames() {
        _selectedUsernames.value = emptySet()
    }

    fun onIntent(intent: EmployeeListIntent) {
        when (intent) {
            is EmployeeListIntent.LoadMore -> {
                val state = currentContentState
                if (state != null && !state.isLastPage && actualResult.lastOrNull() !is EmployeeSearchState.Item.Loading) {
                    val currentCount = actualResult.filterIsInstance<EmployeeSearchState.Item.Employee>().size
                    getData(query = currentSearchQuery, offset = currentCount)
                }
            }
            is EmployeeListIntent.Refresh -> {
                getData(query = currentSearchQuery, offset = 0)
            }
        }
    }

    fun toggleUserSelection(username: String) {
        val current = _selectedUsernames.value
        _selectedUsernames.value = if (current.contains(username)) {
            current - username
        } else {
            current + username
        }
        android.util.Log.d("KTOR", "Current set after click: ${_selectedUsernames.value}")
    }

    private fun getData(query: String? = "", offset: Int = 0) {
        val isFirstPage = offset == 0
        viewModelScope.launch {
            if (isFirstPage) {
                _uiState.emit(EmployeeSearchState.Loading)
            } else {
                mutex.withLock {
                    dropLastTempItem()
                    actualResult.add(EmployeeSearchState.Item.Loading)
                    updateContentState()
                }
            }

            searchEmployeesUseCase.invoke(offset, query = query).fold(
                onSuccess = { data ->
                    addItemsToState(data, isFirstPage)
                },
                onFailure = { e ->
                    handleFailure(e, isFirstPage)
                }
            )
        }
    }

    private suspend fun addItemsToState(data: PagingEmployeeListEntity, isFirstPage: Boolean) {
        mutex.withLock {
            if (isFirstPage) {
                actualResult.clear()
            } else {
                dropLastTempItem()
            }

            actualResult.addAll(data.users.map { EmployeeSearchState.Item.Employee(it) })

            _uiState.emit(EmployeeSearchState.Content(
                users = actualResult.toPersistentList(),
                isLastPage = data.isLast
            ))
        }
    }

    private suspend fun handleFailure(e: Throwable, isFirstPage: Boolean) {
        if (isFirstPage) {
            _uiState.emit(EmployeeSearchState.Error(e.message ?: "Unknown Error"))
        } else {
            mutex.withLock {
                dropLastTempItem()
                actualResult.add(EmployeeSearchState.Item.Error)
                updateContentState()
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            snapshotFlow { searchState.text }
                .map { it.toString().trim() }
                .distinctUntilChanged()
                .debounce(400)
                .collect { query ->
                    if (query.matches(validationRegex)) {
                        currentSearchQuery = query
                        getData(query, offset = 0)
                    } else {
                        _uiState.emit(
                            currentContentState?.copy(inputError = "Недопустимые символы")
                                ?: EmployeeSearchState.Content(
                                    users = emptyList<EmployeeSearchState.Item>().toPersistentList(),
                                    isLastPage = true,
                                    inputError = "Недопустимые символы"
                                )
                        )
                    }
                }
        }
    }


    private fun dropLastTempItem() {
        if (actualResult.isNotEmpty()) {
            val last = actualResult.last()
            if (last is EmployeeSearchState.Item.Loading || last is EmployeeSearchState.Item.Error) {
                actualResult.removeAt(actualResult.lastIndex)
            }
        }
    }

    private suspend fun updateContentState() {
        val currentState = _uiState.value
        if (currentState is EmployeeSearchState.Content) {
            _uiState.emit(currentState.copy(users = actualResult.toPersistentList()))
        }
    }
    fun createMeeting(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val usernames = _selectedUsernames.value.toList()

            val dateText = dateState.text.toString()
            val timeText = timeState.text.toString()

            if (dateText.isBlank() || timeText.isBlank()) {
                _uiState.emit(EmployeeSearchState.Error("Выберите дату и время"))
                return@launch
            }

            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val startDateTime = LocalDateTime.parse("${dateText}T${timeText}:00", formatter)

                val endDateTime = startDateTime.plusHours(1)

                val startTimeIso = startDateTime.format(formatter)
                val endTimeIso = endDateTime.format(formatter)

                android.util.Log.d("DEBUG_TAG", "Start: $startTimeIso, End: $endTimeIso")

                val draft = CreateMeetingEntity(
                    name = titleState.text.toString(),
                    description = locationState.text.toString(),
                    startTime = startTimeIso,
                    endTime = endTimeIso
                )

                createMeetingUseCase(draft, usernames).onSuccess {
                    onSuccess()
                }.onFailure { e ->
                    _uiState.emit(EmployeeSearchState.Error(e.message ?: "Ошибка при создании"))
                }
            } catch (_: Exception) {
                _uiState.emit(EmployeeSearchState.Error("Ошибка формата даты"))
            }
        }
    }
    }