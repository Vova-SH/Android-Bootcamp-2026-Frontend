package ru.sicampus.bootcamp2026.domain.users

import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.domain.users.entities.PagingMeetingListEntity

class GetMeetingsUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(
        offset: Int
    ): Result<PagingMeetingListEntity> {
        return meetingRepository.getMeetings(
            page = offset / COUNT,
            size = COUNT,
        )
    }

    private companion object {
        const val COUNT = 20
    }
}