package com.example.planify.main.features.meetings.data.repositories_impl

import com.example.planify.main.features.auth.domain.utils.network.AuthenticatedApiClient
import com.example.planify.main.features.meetings.data.dto.create_invite.InviteUserRequestDTO
import com.example.planify.main.features.meetings.data.dto.create_invite.InviteUserResponseDTO
import com.example.planify.main.features.meetings.data.dto.get_invite.GetInviteDTO
import com.example.planify.main.features.meetings.data.dto.reschedule_invite.InviteRescheduleRequestDTO
import com.example.planify.main.features.meetings.data.dto.reschedule_invite.InviteRescheduleResponseDTO
import com.example.planify.main.features.meetings.domain.entities.MeetingInvite
import com.example.planify.main.features.meetings.domain.repositories.MeetingInvitesRepository
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingInvitesRepositoryImpl @Inject constructor(
    val authenticatedApiClient: AuthenticatedApiClient
): MeetingInvitesRepository {

    private val meetingInvitesFeaturePath = "/meetings/invites"

    private val createInvitePath = meetingInvitesFeaturePath
    private val meetingInvitesRescheduleResponsePath = "$meetingInvitesFeaturePath/%s/reschedule/response"
    private val meetingInvitesRescheduleRequestPath = "$meetingInvitesFeaturePath/%s/reschedule/request"
    private val meetingInvitesRejectPath = "$meetingInvitesFeaturePath/%s/reject"
    private val meetingInvitesAcceptPath = "$meetingInvitesFeaturePath/%s/accept"
    private val meetingInviteGetPath = "$meetingInvitesFeaturePath/%s"

    override suspend fun inviteUser(meetingId: Long, targetUserId: Long): Result<MeetingInvite> = withContext(Dispatchers.IO) {
        val requestDTO = InviteUserRequestDTO(
            meetingId = meetingId,
            targetId = targetUserId
        )

        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<InviteUserResponseDTO> {
                method = HttpMethod.Post
                url { path(createInvitePath) }
                setBody(requestDTO)
            }

            response.invite.toEntity()
        }
    }

    override suspend fun inviteRescheduleResponse(inviteUuid: String, shouldReschedule: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        val requestDTO = InviteRescheduleResponseDTO(
            shouldReschedule = shouldReschedule
        )

        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Post
                url { path(meetingInvitesRescheduleResponsePath.format(inviteUuid)) }
                setBody(requestDTO)
            }
        }
    }

    override suspend fun inviteRescheduleRequest(inviteUuid: String, rescheduleTo: LocalDateTime): Result<Unit> = withContext(Dispatchers.IO) {
        val requestDTO = InviteRescheduleRequestDTO(
            rescheduleTo = rescheduleTo
        )

        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Post
                url { path(meetingInvitesRescheduleRequestPath.format(inviteUuid)) }
                setBody(requestDTO)
            }
        }
    }

    override suspend fun inviteReject(inviteUuid: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Post
                url { path(meetingInvitesRejectPath.format(inviteUuid)) }
            }
        }
    }

    override suspend fun inviteAccept(inviteUuid: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Post
                url { path(meetingInvitesAcceptPath.format(inviteUuid)) }
            }
        }
    }

    override suspend fun getInvite(inviteUuid: String): Result<MeetingInvite> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<GetInviteDTO> {
                method = HttpMethod.Get
                url { path(meetingInviteGetPath.format(inviteUuid)) }
            }

            response.invite.toEntity()
        }
    }
}