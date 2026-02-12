package ru.sicampus.bootcamp2026.domain.mymeetings

import ru.sicampus.bootcamp2026.data.EventRepository

class DeleteMeetingUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        meetingId: Int,
    ): Result<Unit>{
        return eventRepository.deleteMeeting(meetingId)
    }
}