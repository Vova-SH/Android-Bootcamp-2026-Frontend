package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class DeleteMeetingUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(meetingId: Long): Result<Unit> {
        return repository.deleteMeeting(meetingId)
    }
}