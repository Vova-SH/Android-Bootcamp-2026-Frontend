package ru.innovationcampus.android.domain.meetings

import ru.innovationcampus.android.data.MeetingRepository
import ru.innovationcampus.android.data.UserRepository
import ru.innovationcampus.android.domain.list.entities.PagingUserListEntity
import ru.innovationcampus.android.domain.meetings.entities.PagingMeetingListEntity

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