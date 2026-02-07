package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.mappers.toDto
import ru.sicampus.bootcamp2026.data.mappers.toEntity
import ru.sicampus.bootcamp2026.data.source.InvitationApiService
import ru.sicampus.bootcamp2026.data.source.MeetingApiService
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

class MeetingRepository(
    private val meetingApiService: MeetingApiService = MeetingApiService(),
    private val invitationApiService: InvitationApiService = InvitationApiService()
) {
    suspend fun getMeetings(page: Int = 0, size: Int = 20): Result<List<MeetingEntity>> {
        return try {
            val meetings = meetingApiService.getMeetings(page, size)
            Result.success(meetings.map { it.toEntity() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyMeetings(page: Int = 0, size: Int = 20): Result<List<MeetingEntity>> {
        return try {
            val meetings = meetingApiService.getMyMeetings(page, size)
            Result.success(meetings.map { it.toEntity() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMeeting(meeting: MeetingEntity): Result<MeetingEntity> {
        return try {
            val created = meetingApiService.createMeeting(meeting.toDto())
            Result.success(created.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createInvitation(invitation: InvitationEntity): Result<InvitationEntity> {
        return try {
            val created = invitationApiService.createInvitation(invitation.toDto())
            Result.success(created.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyInvitations(page: Int = 0, size: Int = 20): Result<List<InvitationEntity>> {
        return try {
            val invitations = invitationApiService.getMyInvitations(page, size)
            Result.success(invitations.map { it.toEntity() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateInvitation(invitation: InvitationEntity): Result<InvitationEntity> {
        return try {
            val updated = invitationApiService.updateInvitation(invitation.id, invitation.toDto())
            Result.success(updated.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
