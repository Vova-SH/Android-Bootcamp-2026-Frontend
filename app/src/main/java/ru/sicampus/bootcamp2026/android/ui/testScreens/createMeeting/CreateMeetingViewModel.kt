package ru.sicampus.bootcamp2026.android.ui.testScreens.createMeeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import ru.sicampus.bootcamp2026.android.data.CreateMeetingRepository
import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.InvitationNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.MeetingsNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.PersonsNetworkDataSource
import ru.sicampus.bootcamp2026.android.domain.invitation.CreateMeetingWithInvitationsUseCase
import ru.sicampus.bootcamp2026.android.domain.invitation.SearchPersonByEmailUseCase
import java.time.*
import java.time.format.DateTimeFormatter
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.R


class CreateMeetingViewModel : ViewModel() {

    private val repository by lazy {
        CreateMeetingRepository(
            meetingsNetworkDataSource = MeetingsNetworkDataSource(),
            invitationNetworkDataSource = InvitationNetworkDataSource(),
            personNetworkDataSource = PersonsNetworkDataSource(),
            authLocalDataSource = AuthLocalDataSource
        )
    }

    private val searchPersonByEmailUseCase by lazy {
        SearchPersonByEmailUseCase(repository)
    }

    private val createMeetingWithInvitationsUseCase by lazy {
        CreateMeetingWithInvitationsUseCase(repository)
    }

    private val _uiState = MutableStateFlow<CreateMeetingState>(
        CreateMeetingState.Data()
    )
    val uiState: StateFlow<CreateMeetingState> = _uiState.asStateFlow()

    private val _actionFlow = MutableSharedFlow<CreateMeetingAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    private var searchJob: Job? = null
    private val SEARCH_DELAY_MS = 500L

    private fun str(id: Int): String = App.context.getString(id)

    fun onIntent(intent: CreateMeetingIntent) {
        when (intent) {
            is CreateMeetingIntent.UpdateTitle -> {
                updateStateIfData { it.copy(title = intent.title, error = null) }
                validateForm()
            }

            is CreateMeetingIntent.UpdateDescription -> {
                updateStateIfData { it.copy(description = intent.description, error = null) }
                validateForm()
            }

            is CreateMeetingIntent.UpdateDate -> {
                updateStateIfData { it.copy(date = intent.date, error = null) }
                validateForm()
            }

            is CreateMeetingIntent.UpdateStartHour -> {
                updateStateIfData { it.copy(startHour = intent.hour, error = null) }
                validateForm()
            }

            is CreateMeetingIntent.UpdateEndHour -> {
                updateStateIfData { it.copy(endHour = intent.hour, error = null) }
                validateForm()
            }

            is CreateMeetingIntent.UpdateSearchQuery -> {
                updateStateIfData {
                    it.copy(searchQuery = intent.query)
                }
                searchWithDebounce(intent.query)
            }

            is CreateMeetingIntent.SearchPerson -> searchPerson()

            is CreateMeetingIntent.AddParticipant -> {
                updateStateIfData { old ->
                    old.copy(
                        participants = old.participants + intent.participant,
                        searchQuery = "",
                        searchResults = emptyList(),
                        error = null
                    )
                }
                validateForm()
            }

            is CreateMeetingIntent.RemoveParticipant -> {
                updateStateIfData { old ->
                    old.copy(
                        participants = old.participants.filter { it.id != intent.participantId },
                        error = null
                    )
                }
                validateForm()
            }

            is CreateMeetingIntent.CreateMeeting -> createMeeting()
        }
    }

    private fun searchWithDebounce(query: String) {
        searchJob?.cancel()

        val trimmedQuery = query.trim()

        if (trimmedQuery.length < 3) {
            updateStateIfData {
                it.copy(
                    searchResults = emptyList(),
                    isSearching = false
                )
            }
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY_MS)
            searchPersonInternal(trimmedQuery)
        }
    }

    private fun searchPerson() {
        val state = (_uiState.value as? CreateMeetingState.Data) ?: return
        val query = state.searchQuery.trim()
        if (query.isBlank()) return
        searchJob?.cancel()
        viewModelScope.launch {
            searchPersonInternal(query)
        }
    }

    private suspend fun searchPersonInternal(query: String) {
        val state = (_uiState.value as? CreateMeetingState.Data) ?: return

        if (query.length < 3) {
            updateStateIfData {
                it.copy(
                    isSearching = false,
                    searchResults = emptyList()
                )
            }
            return
        }

        updateStateIfData { it.copy(isSearching = true) }

        searchPersonByEmailUseCase.invoke(query).fold(
            onSuccess = { persons ->
                val filteredPersons = persons.filter { person ->
                    state.participants.none { it.id == person.id }
                }

                updateStateIfData {
                    it.copy(
                        isSearching = false,
                        searchResults = filteredPersons,
                        error = null
                    )
                }
            },
            onFailure = {
                updateStateIfData {
                    it.copy(
                        isSearching = false,
                        searchResults = emptyList()
                    )
                }
            }
        )
    }

    private fun validateForm() {
        updateStateIfData {
            val hint = getValidationHint(it)
            it.copy(
                isEnabledCreate = hint == null,
                validationHint = hint
            )
        }
    }

    private fun createMeeting() {
        val state = (_uiState.value as? CreateMeetingState.Data) ?: return

        val validationHint = getValidationHint(state)
        if (validationHint != null) {
            updateStateIfData { it.copy(error = validationHint) }
            return
        }

        _uiState.value = CreateMeetingState.Loading

        viewModelScope.launch {
            try {
                val startAt = convertToIsoUtc(state.date, state.startHour)
                val endAt = convertToIsoUtc(state.date, state.endHour)

                createMeetingWithInvitationsUseCase.invoke(
                    title = state.title.trim(),
                    description = state.description.trim(),
                    startAt = startAt,
                    endAt = endAt,
                    inviteeIds = state.participants.map { it.id }
                ).fold(
                    onSuccess = {
                        _actionFlow.emit(CreateMeetingAction.MeetingCreated)
                    },
                    onFailure = {
                        _uiState.value = state.copy(
                            error = it.message ?: str(R.string.cm_error_create_failed)
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    error = e.message ?: str(R.string.cm_error_unknown)
                )
            }
        }
    }

    private fun convertToIsoUtc(date: String, hour: String): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val localDate = LocalDate.parse(date, dateFormatter)
        val localTime = LocalTime.of(hour.toInt(), 0)
        val localDateTime = LocalDateTime.of(localDate, localTime)

        return localDateTime
            .atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)
    }

    private fun updateStateIfData(
        block: (CreateMeetingState.Data) -> CreateMeetingState
    ) {
        _uiState.update { state ->
            (state as? CreateMeetingState.Data)?.let(block) ?: state
        }
    }

    private fun getValidationHint(state: CreateMeetingState.Data): String? {
        return when {
            state.title.isBlank() -> str(R.string.cm_validation_title)
            state.description.isBlank() -> str(R.string.cm_validation_description)
            state.date.isBlank() -> str(R.string.cm_validation_date)
            state.startHour.isBlank() -> str(R.string.cm_validation_start_time)
            state.endHour.isBlank() -> str(R.string.cm_validation_end_time)

            state.startHour.toIntOrNull() == null || state.endHour.toIntOrNull() == null ->
                str(R.string.cm_validation_start_time)

            state.endHour.toInt() <= state.startHour.toInt() ->
                str(R.string.cm_validation_end_after_start)

            state.participants.isEmpty() -> str(R.string.cm_validation_add_participant)
            else -> null
        }
    }
}