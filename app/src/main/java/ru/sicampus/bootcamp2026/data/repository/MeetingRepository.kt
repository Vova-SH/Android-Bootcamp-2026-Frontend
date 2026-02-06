package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.source.MeetingInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

class MeetingRepository(
    private val meetingInfoDataSource: MeetingInfoDataSource,
) {
    suspend fun getMeetings(): Result<List<MeetingEntity>> {
        return meetingInfoDataSource.getMeeting().map { listDto ->
            listDto.mapNotNull { meetingDto ->
                MeetingEntity(
                    id = meetingDto.id ?: return@mapNotNull null,
                    startTime = meetingDto.startTime ?: return@mapNotNull null,
                    endTime = meetingDto.endTime ?: return@mapNotNull null,
                    name = meetingDto.name ?: return@mapNotNull null,
                    date = meetingDto.date ?: return@mapNotNull null,
                    participants = meetingDto.participants ?: return@mapNotNull null
                )
            }
        }
    }

    suspend fun getMeetingsCalendar(): Result<List<MeetingEntity>> {
        return getMeetings().map { listDto ->
            listDto.map { it }.sortedBy { it.id }
        }
    }

    suspend fun getInvitedMeetings(UserId: Int): Result<List<MeetingEntity>> {
        return getMeetings().map { listDto ->
            listDto.filter { it.participants.contains(UserId) }.sortedBy { it.id }.map { it }
        }
    }

    suspend fun createMeeting(
        name: String?,
        startTime: Byte?,
        endTime: Byte?,
        date: String?,
        participants: List<Int>?,
    ): Result<Unit> {
        return meetingInfoDataSource.createMeeting(name,
            startTime, endTime, date, participants).map {

        }
    }
}
