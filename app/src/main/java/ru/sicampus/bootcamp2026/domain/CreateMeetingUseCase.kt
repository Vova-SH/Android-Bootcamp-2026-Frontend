package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import java.time.LocalDate

class CreateMeetingUseCase(
    private val meetingRepository: MeetingRepository = MeetingRepository()
) {
    suspend operator fun invoke(
        title: String,
        organizerId: Long,
        date: LocalDate,
        startHour: Int,
        inviteeIds: List<Long>
    ): Result<Unit> {
        return try {
            val meetingResult = meetingRepository.createMeeting(
                MeetingEntity(
                    id = -1L,
                    title = title,
                    organizerId = organizerId,
                    date = date,
                    startHour = startHour,
                    createdAt = null
                )
            )
            
            val meeting = meetingResult.getOrThrow()
            
            inviteeIds.forEach { inviteeId ->
                meetingRepository.createInvitation(
                    InvitationEntity(
                        id = -1L,
                        meetingId = meeting.id,
                        inviteeId = inviteeId,
                        status = 0, // Pending
                        invitedAt = null,
                        respondedAt = null
                    )
                )
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
