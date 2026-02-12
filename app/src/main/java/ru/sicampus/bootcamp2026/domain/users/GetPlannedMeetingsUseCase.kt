package ru.sicampus.bootcamp2026.domain.users

import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.domain.users.entities.PagingMeetingListEntity

class GetPlannedMeetingsUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(
        offset: Int
    ): Result<PagingMeetingListEntity> {
        return meetingRepository.getPlannedMeetings(
            page = offset / COUNT,
            size = COUNT,
            userId = App.user.id
        )
    }

    private companion object {
        const val COUNT = 20
    }
}