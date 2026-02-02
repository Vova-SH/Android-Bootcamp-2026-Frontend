package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

class GetMeetingsUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): Result<List<MeetingEntity>> {
        return meetingRepository.getMeetings()
    }
}