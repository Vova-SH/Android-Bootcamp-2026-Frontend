package ru.innovationcampus.android.data

import ru.innovationcampus.android.data.source.MeetingInfoDataSource
import ru.innovationcampus.android.data.source.UserInfoDataSource
import ru.innovationcampus.android.domain.meetings.entities.PagingMeetingListEntity
import ru.innovationcampus.android.domain.list.entities.PagingUserListEntity
import ru.innovationcampus.android.domain.list.entities.UserEntity
import ru.innovationcampus.android.domain.meetings.entities.MeetingEntity

class MeetingRepository(
    private val meetingInfoDataSource: MeetingInfoDataSource
) {
    suspend fun getMeetings(
        page: Int,
        size: Int
    ): Result<PagingMeetingListEntity> {
        return meetingInfoDataSource.getMeeting(
            page = page,
            size = size,
        ).mapCatching { dto ->
            PagingMeetingListEntity(
                isLast = dto.last ?: true,
                meetings = dto.content?.mapNotNull { meetingDto ->
                    MeetingEntity(
                        creatorName = meetingDto.creatorName ?: return@mapNotNull null,
                        date = meetingDto.date ?: return@mapNotNull null,
                        title = meetingDto.title ?: return@mapNotNull null,
                        description = meetingDto.description ?: return@mapNotNull null,
                    )
                } ?: error("List is null")
            )
        }
    }
}