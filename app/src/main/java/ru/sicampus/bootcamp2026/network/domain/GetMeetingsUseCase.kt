package ru.sicampus.bootcamp2026.network.domain

import ru.sicampus.bootcamp2026.network.data.MeetingRepository
import ru.sicampus.bootcamp2026.network.domain.entities.MeetingEntity

class GetMeetingsUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): Result<List<MeetingEntity>> {
        return meetingRepository.getMeetings()
    }
}