package ru.sicampus.bootcamp2026.ui.meeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.repository.ProfileRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel для экрана создания встречи
 */
@HiltViewModel
class CreateMeetingViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateMeetingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAvailableParticipants()
    }

    fun onEvent(event: CreateMeetingUiEvent) {
        when (event) {
            is CreateMeetingUiEvent.TitleChanged -> {
                _uiState.update { it.copy(title = event.title, titleError = null) }
            }
            is CreateMeetingUiEvent.DescriptionChanged -> {
                _uiState.update { it.copy(description = event.description) }
            }
            is CreateMeetingUiEvent.LocationChanged -> {
                _uiState.update { it.copy(location = event.location) }
            }
            is CreateMeetingUiEvent.ParticipantToggled -> {
                toggleParticipant(event.userId)
            }
            is CreateMeetingUiEvent.DateChanged -> {
                _uiState.update { it.copy(selectedDate = event.date) }
                // Автоматически перезагружаем свободное время при смене даты
                if (_uiState.value.selectedParticipants.isNotEmpty()) {
                    loadFreeTime()
                }
            }
            is CreateMeetingUiEvent.TimeSlotSelected -> {
                _uiState.update { it.copy(selectedTimeSlot = event.timeSlot, timeSlotError = null) }
            }
            is CreateMeetingUiEvent.NextStep -> {
                nextStep()
            }
            is CreateMeetingUiEvent.PreviousStep -> {
                previousStep()
            }
            is CreateMeetingUiEvent.LoadFreeTime -> {
                loadFreeTime()
            }
            is CreateMeetingUiEvent.CreateMeeting -> {
                createMeeting()
            }
            is CreateMeetingUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun loadAvailableParticipants() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = profileRepository.getAllUsers(page = 0, size = 100)
            when (result) {
                is Result.Success -> {
                    val participants = result.data.content.map { user ->
                        ParticipantItem(
                            id = user.id,
                            username = user.username,
                            isSelected = false
                        )
                    }
                    _uiState.update {
                        it.copy(
                            availableParticipants = participants,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to load users: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    private fun toggleParticipant(userId: UUID) {
        _uiState.update { state ->
            val updatedParticipants = state.availableParticipants.map { participant ->
                if (participant.id == userId) {
                    participant.copy(isSelected = !participant.isSelected)
                } else {
                    participant
                }
            }

            val selectedIds = updatedParticipants
                .filter { it.isSelected }
                .map { it.id.toString() }  // Преобразуем UUID в String

            state.copy(
                availableParticipants = updatedParticipants,
                selectedParticipants = selectedIds,
                participantsError = null
            )
        }
    }

    private fun nextStep() {
        val state = _uiState.value

        when (state.currentStep) {
            CreateMeetingStep.BASIC_INFO -> {
                // Валидация базовой информации
                if (state.title.isBlank()) {
                    _uiState.update { it.copy(titleError = "Title is required") }
                    return
                }
                _uiState.update { it.copy(currentStep = CreateMeetingStep.SELECT_PARTICIPANTS) }
            }
            CreateMeetingStep.SELECT_PARTICIPANTS -> {
                // Валидация участников
                if (state.selectedParticipants.isEmpty()) {
                    _uiState.update { it.copy(participantsError = "At least one participant is required") }
                    return
                }
                // Загружаем свободное время для выбранных участников
                loadFreeTime()
                _uiState.update { it.copy(currentStep = CreateMeetingStep.SELECT_TIME) }
            }
            CreateMeetingStep.SELECT_TIME -> {
                // Создание встречи
                createMeeting()
            }
        }
    }

    private fun previousStep() {
        val state = _uiState.value

        when (state.currentStep) {
            CreateMeetingStep.BASIC_INFO -> {
                // Уже на первом шаге
            }
            CreateMeetingStep.SELECT_PARTICIPANTS -> {
                _uiState.update { it.copy(currentStep = CreateMeetingStep.BASIC_INFO) }
            }
            CreateMeetingStep.SELECT_TIME -> {
                _uiState.update {
                    it.copy(
                        currentStep = CreateMeetingStep.SELECT_PARTICIPANTS,
                        freeTimeSlots = emptyList(),
                        selectedTimeSlot = null
                    )
                }
            }
        }
    }

    private fun loadFreeTime() {
        val state = _uiState.value

        if (state.selectedParticipants.isEmpty()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFreeTime = true) }

            // Преобразуем дату в строку формата yyyy-MM-dd
            val dateString = state.selectedDate.toString()

            val result = meetingRepository.getFreeTime(
                state.selectedParticipants.map { UUID.fromString(it) },  // Преобразуем String в UUID
                dateString
            )
            when (result) {
                is Result.Success -> {
                    val slots = result.data
                        .filter { slot ->
                            // Фильтруем только слоты на выбранную дату
                            slot.startTime.toLocalDate() == state.selectedDate
                        }
                        .map { slot ->
                            TimeSlotItem(
                                startTime = slot.startTime.format(DateTimeFormatter.ISO_DATE_TIME),  // Преобразуем в String
                                endTime = slot.endTime.format(DateTimeFormatter.ISO_DATE_TIME)  // Преобразуем в String
                            )
                        }

                    _uiState.update {
                        it.copy(
                            freeTimeSlots = slots,
                            isLoadingFreeTime = false,
                            selectedTimeSlot = slots.firstOrNull() // Автоматически выбираем первый слот
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to load free time: ${result.exception.message}",
                            isLoadingFreeTime = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    private fun createMeeting() {
        val state = _uiState.value

        // Финальная валидация
        if (state.selectedTimeSlot == null) {
            _uiState.update { it.copy(timeSlotError = "Please select a time slot") }
            return
        }

        if (state.selectedParticipants.isEmpty()) {
            _uiState.update { it.copy(error = "Please select at least one participant") }
            return
        }

        // Создание встречи
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // TimeSlotItem.startTime и endTime уже в формате ISO_DATE_TIME строк
                // Преобразуем их в LocalDateTime для репозитория
                val startDateTime = LocalDateTime.parse(
                    state.selectedTimeSlot.startTime,
                    DateTimeFormatter.ISO_DATE_TIME
                )
                val endDateTime = LocalDateTime.parse(
                    state.selectedTimeSlot.endTime,
                    DateTimeFormatter.ISO_DATE_TIME
                )

                val participantIds = state.selectedParticipants.map { UUID.fromString(it) }

                // Логирование для отладки
                android.util.Log.d("CreateMeeting", """
                    Title: ${state.title}
                    Description: ${state.description}
                    Location: ${state.location}
                    StartTime: ${startDateTime}
                    EndTime: ${endDateTime}
                    ParticipantIds: $participantIds
                    ParticipantsCount: ${participantIds.size}
                """.trimIndent())

                val result = meetingRepository.createMeeting(
                    title = state.title,
                    description = state.description.takeIf { it.isNotBlank() },
                    location = state.location.takeIf { it.isNotBlank() },
                    startTime = startDateTime,
                    endTime = endDateTime,
                    participantIds = participantIds
                )

                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Failed to create meeting: ${result.exception.message}"
                            )
                        }
                    }
                    is Result.Loading -> {
                        // Already handled
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error parsing date/time: ${e.message}"
                    )
                }
                    // Уже обработано выше
                }
            }
        }
    }


