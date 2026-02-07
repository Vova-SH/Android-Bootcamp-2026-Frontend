package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.compose.foundation.text.input.TextFieldState
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
import ru.sicampus.bootcamp2026.data.EmployeeRepository
import ru.sicampus.bootcamp2026.data.source.EmployeeSearchDataSource
import ru.sicampus.bootcamp2026.domain.SearchEmployeesUseCase
import ru.sicampus.bootcamp2026.domain.entities.PagingEmployeeListEntity

class MeetingViewModel : ViewModel() {
    private val mutex = Mutex()
    private val actualResult: MutableList<EmployeeSearchState.Item> = mutableListOf()
    private val searchEmployeesUseCase by lazy{
        SearchEmployeesUseCase(
            employeeRepository = EmployeeRepository(EmployeeSearchDataSource())
        )
    }
    private val _uiState : MutableStateFlow<EmployeeSearchState> =
        MutableStateFlow(EmployeeSearchState.Loading)
    val uiState = _uiState.asStateFlow()

    val searchState = TextFieldState()
    private val validationRegex = Regex("^[a-zA-Zа-яА-ЯёЁ\\s]*$")

    private var currentSearchQuery: String = ""

    init{
        observeSearch()
    }
    private fun getData(query: String? = "", offset: Int = 0){
        val isFirstPage = offset == 0
        viewModelScope.launch {
            _uiState.emit(
                if (isFirstPage){
                    EmployeeSearchState.Loading
                } else{
                    mutex.withLock {
                        dropLastTempItem()
                        actualResult.add(EmployeeSearchState.Item.Loading)
                        (_uiState.value as? EmployeeSearchState.Content)?.copy(
                            users = actualResult.toPersistentList() ) ?: EmployeeSearchState.Loading
                    }

                }
            )
            searchEmployeesUseCase.invoke(offset, query = query).fold(
                onSuccess = {data ->
                    addItemsToState(data, isFirstPage)
                },
                onFailure = {e ->
                    e.printStackTrace()
                    _uiState.emit(when(val state = _uiState.value){
                        is EmployeeSearchState.Content -> {
                            mutex.withLock {
                                dropLastTempItem()
                                actualResult.add(EmployeeSearchState.Item.Error)
                                state.copy(
                                    users = actualResult.toPersistentList()
                                )
                            }

                        }
                        is EmployeeSearchState.Error,
                        EmployeeSearchState.Loading -> {
                            EmployeeSearchState.Error(e.message.orEmpty())
                        }
                    }
                    )
                }
            )
        }
    }

    private suspend fun addItemsToState(data: PagingEmployeeListEntity, isFirstPage: Boolean) {
        mutex.withLock {
            if(isFirstPage){
                actualResult.clear()
            } else{
                dropLastTempItem()
            }
            actualResult.addAll(
                data.users.map{ item ->
                    EmployeeSearchState.Item.Employee(item)

                }
            )
            _uiState.emit(EmployeeSearchState.Content(
                isLastPage = data.isLast,
                users = actualResult.toPersistentList()
            ))
        }

    }
    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            snapshotFlow { searchState.text }
                .map { it.toString() }
                .distinctUntilChanged()
                .debounce(400)
                .collect { query ->
                    if (query.matches(validationRegex)){
                        currentSearchQuery = query
                        getData(query)
                    }
                    else{
                        val currentUsers = (uiState.value as? EmployeeSearchState.Content)?.users ?: emptyList()
                        _uiState.emit(EmployeeSearchState.Content(currentUsers.toPersistentList(), isLastPage = false,
                            inputError = "Строка поиска содержит недопустимые символы"
                        ))
                    }
                }
        }
    }
    private fun dropLastTempItem(){
        when(actualResult.last()){
            is EmployeeSearchState.Item.Employee -> Unit
            is EmployeeSearchState.Item.Error,
            is EmployeeSearchState.Item.Loading -> actualResult.removeAt(actualResult.lastIndex)
        }
    }

    fun onIntent(intent: EmployeeListIntent) {
        when(intent) {
            is EmployeeListIntent.LoadMore -> {
                getData(query = currentSearchQuery, offset = actualResult.size)
            }
            is EmployeeListIntent.Refresh -> {
                getData(
                    query = currentSearchQuery,
                    offset = if (actualResult.isEmpty()) 0 else actualResult.size - 1
                )
            }
        }

    }
}