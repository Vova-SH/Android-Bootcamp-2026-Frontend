package com.example.planify.main.features.meetings.domain.repositories

import com.example.planify.main.features.meetings.domain.entities.MeetingInvite
import java.time.LocalDateTime

interface MeetingInvitesRepository {
    suspend fun inviteUser(meetingId: Long, targetUserId: Long): Result<MeetingInvite>

    suspend fun inviteRescheduleResponse(inviteUuid: String, shouldReschedule: Boolean): Result<Unit>

    suspend fun inviteRescheduleRequest(inviteUuid: String, rescheduleTo: LocalDateTime): Result<Unit>

    suspend fun inviteReject(inviteUuid: String):  Result<Unit>

    suspend fun inviteAccept(inviteUuid: String): Result<Unit>

    suspend fun getInvite(inviteUuid: String): Result<MeetingInvite>
}