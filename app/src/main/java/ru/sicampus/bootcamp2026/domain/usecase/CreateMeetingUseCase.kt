package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class CreateMeetingUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        place: String,
        start: String,
        duration: Int
    ): Result<Unit> {
        return repository.createMeeting(title, description, place, start, duration)
    }
}