package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.network.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

class GetMeetingsForMe(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke() : Result<List<MeetingEntity>> {
        return meetingRepository.getBookingToMe()
    }
}
class GetMeetingsOutMe  (
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke() : Result<List<MeetingEntity>> {
        return meetingRepository.getBookingOutMe()
    }
}