package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.repository.MeetingRepository

class CreateMeetingUseCase (
    private val meetingRepository: MeetingRepository
    ) {
        suspend operator fun invoke(
            name: String?,
            startTime: Byte?,
            endTime: Byte?,
            date: String?,
            participants: List<Int>?
        ): Result<Unit> {
            return meetingRepository.createMeeting(name, startTime, endTime, date, participants)
        }
}