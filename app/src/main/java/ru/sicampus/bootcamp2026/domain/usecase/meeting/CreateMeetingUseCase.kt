package ru.sicampus.bootcamp2026.domain.usecase.meeting

import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * Use case для создания встречи
 */
class CreateMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        location: String?,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        participantIds: List<UUID>
    ): Result<Meeting> {
        // Валидация
        if (title.isBlank()) {
            return Result.Error(Exception("Название встречи не может быть пустым"))
        }
        if (startTime.isAfter(endTime)) {
            return Result.Error(Exception("Время начала не может быть позже времени окончания"))
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            return Result.Error(Exception("Встреча не может быть запланирована в прошлом"))
        }
        if (participantIds.isEmpty()) {
            return Result.Error(Exception("Необходимо пригласить хотя бы одного участника"))
        }

        // Проверка, что встреча начинается ровно в начале часа
        if (startTime.minute != 0 || startTime.second != 0) {
            return Result.Error(Exception("Встреча должна начинаться ровно в начале часа"))
        }

        return meetingRepository.createMeeting(
            title = title,
            description = description,
            location = location,
            startTime = startTime,
            endTime = endTime,
            participantIds = participantIds
        )
    }
}

