package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.MeetingInfoDataSource
import ru.sicampus.bootcamp2026.domain.users.entities.MeetingEntity
import ru.sicampus.bootcamp2026.domain.users.entities.PagingMeetingListEntity

class MeetingRepository(
    private val meetingInfoDataSource: MeetingInfoDataSource
) {
    suspend fun getPlannedMeetings(
        page: Int,
        size: Int,
        userId: Int
    ): Result<PagingMeetingListEntity> {
        return meetingInfoDataSource.getPlannedMeetings(
            page = page,
            size = size,
            userId = userId
        ).mapCatching { dto ->
            PagingMeetingListEntity(
                isLast = dto.last ?: true,
                meetings = dto.content?.mapNotNull { meetingDto ->
                    MeetingEntity(
                        title = meetingDto.title ?: return@mapNotNull null,
                        date = meetingDto.date ?: return@mapNotNull null,
                        startTime = meetingDto.startTime ?: return@mapNotNull null,
                        endTime = meetingDto.endTime ?: return@mapNotNull null,
                        creatorId = meetingDto.creatorId ?: return@mapNotNull null,
                        id = meetingDto.id ?: return@mapNotNull null
                    )
                } ?: error("List is null")
            )
        }
    }
    suspend fun getMeetings(
        page: Int,
        size: Int
    ): Result<PagingMeetingListEntity> {
        return meetingInfoDataSource.getMeetings(
            page = page,
            size = size,
        ).mapCatching { dto ->
            PagingMeetingListEntity(
                isLast = dto.last ?: true,
                meetings = dto.content?.mapNotNull { meetingDto ->
                    MeetingEntity(
                        title = meetingDto.title ?: return@mapNotNull null,
                        date = meetingDto.date ?: return@mapNotNull null,
                        startTime = meetingDto.startTime ?: return@mapNotNull null,
                        endTime = meetingDto.endTime ?: return@mapNotNull null,
                        creatorId = meetingDto.creatorId ?: return@mapNotNull null,
                        id = meetingDto.id ?: return@mapNotNull null
                    )
                } ?: error("List is null")
            )
        }
    }
}