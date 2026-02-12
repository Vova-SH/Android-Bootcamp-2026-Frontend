package ru.sicampus.bootcamp2026.domain.usecase.meeting

import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.Meeting
import ru.sicampus.bootcamp2026.domain.entities.MeetingCreate

class CreateMeetingUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingData: MeetingCreate): Result<Meeting> {
        return meetingRepository.createMeeting(meetingData)
    }
}