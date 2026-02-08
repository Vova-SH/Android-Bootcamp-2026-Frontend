package com.example.planify.main.features.meetings.domain.services

import com.example.planify.main.features.meetings.domain.entities.MeetingInvite
import java.time.LocalDateTime

interface MeetingInvitesService {
    suspend fun inviteUser(meetingId: Long, targetId: Long): Result<MeetingInvite>

    suspend fun inviteRescheduleResponse(inviteUuid: String, shouldReschedule: Boolean, actionId: String): Result<Unit>

    suspend fun inviteRescheduleRequest(inviteUuid: String, rescheduleTo: LocalDateTime, actionId: String): Result<Unit>

    suspend fun inviteReject(inviteUuid: String, actionId: String): Result<Unit>

    suspend fun inviteAccept(inviteUuid: String, actionId: String): Result<Unit>

    suspend fun getInvite(inviteUuid: String): Result<MeetingInvite>
}