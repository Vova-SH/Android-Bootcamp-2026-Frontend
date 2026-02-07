package com.teto.planner.data.repository

import com.teto.planner.data.remote.dto.InvitationsPageDto
import com.teto.planner.data.remote.dto.MeetingsPageDto
import com.teto.planner.data.remote.dto.meeting.IntersectionResponseDto
import com.teto.planner.data.remote.dto.meeting.InvitationResponseRequest
import com.teto.planner.data.remote.dto.meeting.MeetingCreateRequest
import com.teto.planner.data.remote.dto.meeting.MeetingDto
import com.teto.planner.data.remote.dto.meeting.MeetingParticipantDto
import com.teto.planner.data.remote.dto.meeting.toDomain
import com.teto.planner.data.remote.dto.toDomain
import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.meeting.IntersectionResponse
import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.model.meeting.MeetingParticipant
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.repository.MeetingRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import java.time.LocalDate
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : MeetingRepository {

    override suspend fun getMeetings(
        startDate: LocalDate,
        endDate: LocalDate,
        includePending: Boolean
    ): Result<PagedList<Meeting>> = runCatching {
        val response = client.get("api/meetings") {
            parameter("startDate", startDate.toString())
            parameter("endDate", endDate.toString())
            parameter("includePending", includePending)
        }.body<MeetingsPageDto>()

        PagedList(
            items = response.items.map { it.toDomain() },
            meta = response.meta?.toDomain() ?: PageMeta(0, 50, response.items.size.toLong())
        )
    }

    override suspend fun getMeeting(id: String): Result<Meeting> = runCatching {
        client.get("api/meetings/$id")
            .body<MeetingDto>()
            .toDomain()
    }

    override suspend fun createMeeting(
        date: LocalDate,
        startTimeHour: Int,
        durationHours: Int,
        title: String,
        description: String?,
        roomId: String?,
        participantIds: List<String>
    ): Result<Meeting> = runCatching {
        val request = MeetingCreateRequest(
            meetingDate = date.toString(),
            startHour = startTimeHour,
            durationHours = durationHours,
            title = title,
            description = description,
            roomId = roomId,
            participantIds = participantIds
        )
        client.post("api/meetings") {
            contentType(Json)
            setBody(request)
        }.body<MeetingDto>().toDomain()
    }

    override suspend fun getIntersection(
        date: LocalDate,
        userIds: List<String>
    ): Result<IntersectionResponse> = runCatching {
        client.get("api/schedule/intersection") {
            parameter("meetingDate", date.toString())
            userIds.forEach { id -> parameter("userIds", id) }
        }.body<IntersectionResponseDto>().toDomain()
    }

    override suspend fun cancelMeeting(id: String): Result<Unit> = runCatching {
        client.delete("api/meetings/$id")
        Unit
    }

    override suspend fun listInvitations(status: ParticipantStatus): Result<PagedList<Invitation>> =
        runCatching {
            val response = client.get("api/invitations") {
                parameter("status", status.name)
            }.body<InvitationsPageDto>()

            PagedList(
                items = response.items.map { it.toDomain() },
                meta = response.meta?.toDomain() ?: PageMeta(0, 50, response.items.size.toLong())
            )
        }

    override suspend fun respondToInvitation(
        meetingId: String,
        status: ParticipantStatus
    ): Result<MeetingParticipant> = runCatching {
        val request = InvitationResponseRequest(status = status.name)
        client.post("api/invitations/$meetingId/response") {
            contentType(Json)
            setBody(request)
        }.body<MeetingParticipantDto>().toDomain()
    }
}