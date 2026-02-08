package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CreateMeetingUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        place: String,
        start: String,
        duration: Int
    ): Result<Unit> {
        if (title.isBlank()) return Result.failure(IllegalArgumentException("Тема встречи обязательна"))
        if (place.isBlank()) return Result.failure(IllegalArgumentException("Место проведения обязательно"))
        if (duration <= 0) return Result.failure(IllegalArgumentException("Длительность должна быть положительной"))

        try {
            val meetingTime = LocalDateTime.parse(start, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            if (meetingTime.isBefore(LocalDateTime.now())) {
                return Result.failure(IllegalArgumentException("Нельзя создать встречу в прошлом"))
            }
        } catch (e: DateTimeParseException) {
            return Result.failure(IllegalArgumentException("Неверный формат даты. Используйте Picker."))
        }

        return repository.createMeeting(title, description, place, start, duration)
    }
}