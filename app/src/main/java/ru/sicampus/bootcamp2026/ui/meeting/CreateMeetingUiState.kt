package ru.sicampus.bootcamp2026.ui.meeting

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

/**
 * UI состояние для экрана создания встречи
 */
data class CreateMeetingUiState(
    // Базовая информация
    val title: String = "",
    val description: String = "",
    val location: String = "",

    // Выбор участников
    val availableParticipants: List<ParticipantItem> = emptyList(),
    val selectedParticipants: List<UUID> = emptyList(),

    // Выбор времени из доступных слотов
    val selectedDate: LocalDate = LocalDate.now(),
    val freeTimeSlots: List<TimeSlotItem> = emptyList(),
    val selectedTimeSlot: TimeSlotItem? = null,
    val isLoadingFreeTime: Boolean = false,

    // Ошибки валидации
    val titleError: String? = null,
    val participantsError: String? = null,
    val timeSlotError: String? = null,

    // Состояние загрузки и ошибки
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,

    // Текущий шаг
    val currentStep: CreateMeetingStep = CreateMeetingStep.BASIC_INFO
)

/**
 * Шаги создания встречи
 */
enum class CreateMeetingStep {
    BASIC_INFO,      // Название, описание, локация
    SELECT_PARTICIPANTS,  // Выбор участников
    SELECT_TIME      // Выбор времени из доступных слотов
}

/**
 * Элемент для отображения участника с чекбоксом
 */
data class ParticipantItem(
    val id: UUID,
    val username: String,
    val isSelected: Boolean = false
)

/**
 * Элемент для отображения временного слота
 */
data class TimeSlotItem(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
) {
    val displayTime: String
        get() = "${startTime.toLocalTime()} - ${endTime.toLocalTime()}"
}

/**
 * События UI для создания встречи
 */
sealed interface CreateMeetingUiEvent {
    data class TitleChanged(val title: String) : CreateMeetingUiEvent
    data class DescriptionChanged(val description: String) : CreateMeetingUiEvent
    data class LocationChanged(val location: String) : CreateMeetingUiEvent
    data class ParticipantToggled(val userId: UUID) : CreateMeetingUiEvent
    data class DateChanged(val date: LocalDate) : CreateMeetingUiEvent
    data class TimeSlotSelected(val timeSlot: TimeSlotItem) : CreateMeetingUiEvent
    data object NextStep : CreateMeetingUiEvent
    data object PreviousStep : CreateMeetingUiEvent
    data object LoadFreeTime : CreateMeetingUiEvent
    data object CreateMeeting : CreateMeetingUiEvent
    data object DismissError : CreateMeetingUiEvent
}

