//package ru.sicampus.bootcamp2026.ui.screens.book
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
//import ru.sicampus.bootcamp2026.domain.usecase.CreateMeetingUseCase
//import java.time.LocalDate
//import java.time.LocalDate.now
//import java.time.LocalTime
//
//
//// пока не придумал нормальную реализацию
//data class BookState(
//    val title: String = "",
//    val description: String = "",
//    val selectedDate: LocalDate = now(),
//    val selectedStartTime: LocalTime = LocalTime.now().withMinute(0).plusHours(1),
//    val selectedEndTime: LocalTime = LocalTime.now().withMinute(0).plusHours(2),
//    val cabinet: String = "Не выбрано",
//    val selectedParticipants: List<UserMiniDto> = emptyList(),
//    val searchQuery: String = "",
//    val searchResults: List<UserMiniDto> = emptyList(),
//    val isLoading: Boolean = false,
//    val error: String? = null,
//    val isSuccess: Boolean = false
//)
//
//class BookViewModel(
//    private val createMeetingUseCase: CreateMeetingUseCase
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(BookState())
//    val state: StateFlow<BookState> = _state.asStateFlow()
//
//    fun onTitleChange(title: String) {
//        _state.value = _state.value.copy(title = title)
//    }
//
//    fun onDescriptionChange(description: String) {
//        _state.value = _state.value.copy(description = description)
//    }
//
//    fun onDateChange(date: LocalDate) {
//        _state.value = _state.value.copy(selectedDate = date)
//    }
//
//    fun onStartTimeChange(time: LocalTime) {
//        _state.value = _state.value.copy(selectedStartTime = time)
//    }
//
//    fun onEndTimeChange(time: LocalTime) {
//        _state.value = _state.value.copy(selectedEndTime = time)
//    }
//
//    fun onCabinetChange(cabinet: String) {
//        _state.value = _state.value.copy(cabinet = cabinet)
//    }
//
//    fun onSearchQueryChange(query: String) {
//        _state.value = _state.value.copy(searchQuery = query)
//        if (query.length >= 2) {
//            searchUsers(query)
//        }
//    }
//
//    fun addParticipant(user: UserMiniDto) {
//        if (!_state.value.selectedParticipants.any { it.id == user.id }) {
//            _state.value = _state.value.copy(
//                selectedParticipants = _state.value.selectedParticipants + user
//            )
//        }
//    }
//
//    fun removeParticipant(userId: Long) {
//        _state.value = _state.value.copy(
//            selectedParticipants = _state.value.selectedParticipants.filter { it.id != userId }
//        )
//    }
//
//    fun createMeeting() {
//        viewModelScope.launch {
//            _state.value = _state.value.copy(isLoading = true, error = null)
//
//            val meetingData = mapOf(
//                "title" to _state.value.title,
//                "description" to _state.value.description,
//                "date" to _state.value.selectedDate.toString(),
//                "timeStart" to _state.value.selectedStartTime.toString(),
//                "timeEnd" to _state.value.selectedEndTime.toString(),
//                "address" to _state.value.cabinet,
//                "participantIds" to _state.value.selectedParticipants.map { it.id }
//            )
//
//            val result = createMeetingUseCase(meetingData)
//
//            result.fold(
//                onSuccess = {
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        isSuccess = true,
//                        title = "",
//                        description = "",
//                        selectedParticipants = emptyList()
//                    )
//                },
//                onFailure = { error ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = error.message ?: "Ошибка создания встречи"
//                    )
//                }
//            )
//        }
//    }
//
//    private fun searchUsers(query: String) {
//        viewModelScope.launch {
//            val result = createMeetingUseCase.searchUsers(query)
//
//            result.fold(
//                onSuccess = { users ->
//                    _state.value = _state.value.copy(searchResults = users)
//                },
//                onFailure = {
//                    _state.value = _state.value.copy(searchResults = emptyList())
//                }
//            )
//        }
//    }
//
//    fun clearError() {
//        _state.value = _state.value.copy(error = null)
//    }
//
//    fun clearSuccess() {
//        _state.value = _state.value.copy(isSuccess = false)
//    }
//}