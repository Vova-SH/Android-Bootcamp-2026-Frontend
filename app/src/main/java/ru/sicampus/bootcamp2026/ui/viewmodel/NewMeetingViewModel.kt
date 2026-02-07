import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.auth.NetworkClient
import ru.sicampus.bootcamp2026.data.auth.TokenStorage
import ru.sicampus.bootcamp2026.data.model.SharedEvents
import ru.sicampus.bootcamp2026.data.model.UserDto
import ru.sicampus.bootcamp2026.data.repository.AppRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(FlowPreview::class)
class NewMeetingViewModel(app: Application) : AndroidViewModel(app) {
    private val tokenStorage = TokenStorage(app)
    private val api = NetworkClient.createAppApi()
    private val repo = AppRepository(api, tokenStorage)

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiEvent = MutableStateFlow<String?>(null)
    val uiEvent = _uiEvent.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false

    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500L)
                .collectLatest { query ->
                    resetAndSearch()
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun resetAndSearch() {
        _users.value = emptyList()
        currentPage = 0
        isLastPage = false
        loadNextPage()
    }

    fun loadNextPage() {
        if (_isLoading.value || isLastPage) return
        viewModelScope.launch {
            _isLoading.value = true
            repo.getUsers(currentPage, 20, _searchQuery.value.ifBlank { null })
                .onSuccess { page ->
                    _users.value += page.content
                    isLastPage = page.last
                    currentPage++
                }
            _isLoading.value = false
        }
    }

    fun toggleSelection(userId: Long) {
        val current = _selectedIds.value.toMutableSet()
        if (current.contains(userId)) current.remove(userId) else current.add(userId)
        _selectedIds.value = current
    }

    fun createMeeting(title: String, date: LocalDate, timeRange: String, description: String) {
        if (title.isBlank()) {
            _uiEvent.value = "Введите тему"
            return
        }
        if (_selectedIds.value.isEmpty()) {
            _uiEvent.value = "Выберите участников"
            return
        }

        try {
            val parts = timeRange.split("-")
            val startPart = parts[0].trim()
            val endPart = parts[1].trim()

            val startLocal = LocalDateTime.of(date, LocalTime.parse(startPart))
            val endLocal = LocalDateTime.of(date, LocalTime.parse(endPart))

            val startZoned = ZonedDateTime.of(startLocal, ZoneId.systemDefault())
            val endZoned = ZonedDateTime.of(endLocal, ZoneId.systemDefault())

            val isoStartUTC = startZoned.withZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val isoEndUTC = endZoned.withZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            viewModelScope.launch {
                _isLoading.value = true
                repo.createMeeting(title, isoStartUTC, isoEndUTC, description, _selectedIds.value.toList())
                    .onSuccess {
                        _uiEvent.value = "Встреча создана!"

                        SharedEvents.notifyMeetingsUpdated()
                    }
                    .onFailure {
                        val msg = if (it.message?.contains("409") == true) "Кто-то из участников занят!" else "Ошибка: ${it.message}"
                        _uiEvent.value = msg
                    }
                _isLoading.value = false
            }
        } catch (e: Exception) {
            _uiEvent.value = "Неверный формат времени"
            Log.e("MEETING", "Error", e)
        }
    }

    fun clearEvent() { _uiEvent.value = null }
}