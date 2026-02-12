package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.CreateInvitationRequestDTO
import ru.sicampus.bootcamp2026.data.dto.CreateMeetingRequestDTO
import ru.sicampus.bootcamp2026.data.source.MeetingDataSource
import ru.sicampus.bootcamp2026.domain.entities.CreateMeetingEntity

class MeetingRepository(
    private val meetingDataSource: MeetingDataSource
) {
    suspend fun createMeetingWithInvitations(
        meeting: CreateMeetingEntity,
        participants: List<String>
    ): Result<Unit> {
        val createMeetingResult = meetingDataSource.createMeeting(
            CreateMeetingRequestDTO(
                name = meeting.name,
                description = meeting.description,
                startTime = meeting.startTime,
                endTime = meeting.endTime
            )
        )

        return createMeetingResult.mapCatching { responseDto ->
            if (participants.isNotEmpty()) {
                meetingDataSource.createInvitation(
                    CreateInvitationRequestDTO(
                        meetingId = responseDto.id,
                        employeeUsernames = participants,
                        message = "Приглашаю на встречу: ${meeting.name}"
                    )
                ).getOrThrow()
            }
        }
    }
}