package com.example.planify.main.features.meetings.domain.services_impl

import com.example.planify.main.features.actions.domain.services.ActionsService
import com.example.planify.main.features.meetings.domain.entities.MeetingInvite
import com.example.planify.main.features.meetings.domain.repositories.MeetingInvitesRepository
import com.example.planify.main.features.meetings.domain.services.MeetingInvitesService
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingInvitesServiceImpl @Inject constructor(
    private val meetingInvitesRepository: MeetingInvitesRepository,
    private val actionsService: ActionsService
): MeetingInvitesService {
    override suspend fun inviteUser(meetingId: Long, targetId: Long): Result<MeetingInvite> {
        return meetingInvitesRepository.inviteUser(meetingId = meetingId, targetUserId = targetId)
    }

    override suspend fun getInvite(inviteUuid: String): Result<MeetingInvite> {
        return meetingInvitesRepository.getInvite(inviteUuid = inviteUuid)
    }

    override suspend fun inviteRescheduleResponse(inviteUuid: String, shouldReschedule: Boolean, actionId: String): Result<Unit> {
        return meetingInvitesRepository.inviteRescheduleResponse(inviteUuid = inviteUuid, shouldReschedule = shouldReschedule)
            .onSuccess { actionsService.deleteAction(actionId) }
    }

    override suspend fun inviteRescheduleRequest(inviteUuid: String, rescheduleTo: LocalDateTime, actionId: String): Result<Unit> {
        return meetingInvitesRepository.inviteRescheduleRequest(inviteUuid = inviteUuid, rescheduleTo = rescheduleTo)
            .onSuccess { actionsService.deleteAction(actionId) }
    }

    override suspend fun inviteReject(inviteUuid: String, actionId: String): Result<Unit> {
        return meetingInvitesRepository.inviteReject(inviteUuid = inviteUuid)
            .onSuccess { actionsService.deleteAction(actionId) }
    }

    override suspend fun inviteAccept(inviteUuid: String, actionId: String): Result<Unit> {
        return meetingInvitesRepository.inviteAccept(inviteUuid = inviteUuid)
            .onSuccess { actionsService.deleteAction(actionId) }
    }
}
