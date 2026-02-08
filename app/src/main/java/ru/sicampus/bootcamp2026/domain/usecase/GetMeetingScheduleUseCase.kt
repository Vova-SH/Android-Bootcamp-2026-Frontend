package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class GetMeetingScheduleUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(): Result<List<Meeting>> {
        return repository.getSchedule()
    }
}