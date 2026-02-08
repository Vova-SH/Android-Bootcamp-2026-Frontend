package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class GetMeetingByIdUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(id: Long): Result<Meeting> = repository.getMeetingById(id)
}