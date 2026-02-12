package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.dto.EventDto
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.domain.home.entities.ParticipantEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity
import kotlin.Int
import kotlin.collections.mapNotNull

class EventRepository(
    private val eventInfoDataSource: EventInfoDataSource
) {
    suspend fun getEvents(): Result<List<EventEntity>>{
        return eventInfoDataSource.getEvents().map{ listDto ->
            listDto.mapNotNull { eventDto ->
                EventEntity(
                    id = eventDto.id ?: return@mapNotNull null,
                    title = eventDto.title ?: return@mapNotNull null,
                    description = eventDto.description ?: return@mapNotNull null,
                    organizerName = eventDto.organizerName ?: return@mapNotNull null,
                    date = eventDto.date ?: return@mapNotNull null,
                    startTime = eventDto.startTime ?: return@mapNotNull null,
                    endTime = eventDto.endTime ?: return@mapNotNull null,
                    participants = eventDto.participants?.map { participantDto ->
                        ParticipantEntity(
                            id = participantDto.id ?: return@mapNotNull null,
                            fullName = participantDto.fullName ?: return@mapNotNull null,
                            status = participantDto.status ?: return@mapNotNull null
                        )
                    } ?: emptyList()
                )
            }
        }
    }

    suspend fun getInvitations(): Result<List<EventEntity>>{
        return eventInfoDataSource.getInvitations().map{ listDto ->
            listDto.mapNotNull { eventDto ->
                EventEntity(
                    id = eventDto.id ?: return@mapNotNull null,
                    title = eventDto.title ?: return@mapNotNull null,
                    description = eventDto.description ?: return@mapNotNull null,
                    organizerName = eventDto.organizerName ?: return@mapNotNull null,
                    date = eventDto.date ?: return@mapNotNull null,
                    startTime = eventDto.startTime ?: return@mapNotNull null,
                    endTime = eventDto.endTime ?: return@mapNotNull null,
                    participants = eventDto.participants?.map { participantDto ->
                        ParticipantEntity(
                            id = participantDto.id ?: return@mapNotNull null,
                            fullName = participantDto.fullName ?: return@mapNotNull null,
                            status = participantDto.status ?: return@mapNotNull null
                        )
                    } ?: emptyList()
                )
            }
        }
    }

    suspend fun changeInv(
        meetingId: Int,
        userId: Int,
        response: Boolean
    ): Result<Unit> {
        return eventInfoDataSource.changeInvitations(meetingId, userId,response)
    }

    suspend fun createEvent(
        organizerId: Int,
        title: String,
        description: String,
        date: String,
        startTime: String,
        endTime: String,
        participantsId: List<Int>
    ): Result<Unit>{
        return eventInfoDataSource.createEvent(
            organizerId = organizerId,
            title = title,
            description = description,
            date = date,
            startTime = startTime,
            endTime = endTime,
            participantsId = participantsId
        )
    }

    suspend fun getMeetings(): Result<List<EventEntity>>{
        return eventInfoDataSource.getMeetings().map{ listDto ->
            listDto.mapNotNull { eventDto ->
                EventEntity(
                    id = eventDto.id ?: return@mapNotNull null,
                    title = eventDto.title ?: return@mapNotNull null,
                    description = eventDto.description ?: return@mapNotNull null,
                    organizerName = eventDto.organizerName ?: return@mapNotNull null,
                    date = eventDto.date ?: return@mapNotNull null,
                    startTime = eventDto.startTime ?: return@mapNotNull null,
                    endTime = eventDto.endTime ?: return@mapNotNull null,
                    participants = eventDto.participants?.map { participantDto ->
                        ParticipantEntity(
                            id = participantDto.id ?: return@mapNotNull null,
                            fullName = participantDto.fullName ?: return@mapNotNull null,
                            status = participantDto.status ?: return@mapNotNull null
                        )
                    } ?: emptyList()
                )
            }
        }
    }

    suspend fun deleteMeeting(
        meetingId: Int
    ): Result<Unit> {
        return eventInfoDataSource.deleteMeeting(meetingId)
    }
}