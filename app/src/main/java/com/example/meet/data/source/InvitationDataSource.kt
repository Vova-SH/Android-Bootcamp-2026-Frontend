package com.example.meet.data.source

import com.example.meet.data.dto.InvitationDto
import com.example.meet.data.dto.UpdateInvitationDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class InvitationDataSource {

    suspend fun loadAllInvitations(): List<InvitationDto> = withContext(Dispatchers.IO) {
        // д э б
        println("DEBUG: Загружаем все приглашения...")
        val invitations = Network.getInvitations()
        println("DEBUG: Загружено ${invitations.size} приглашений")
        invitations.forEachIndexed { index, invitation ->
            // д э б
            println("DEBUG: Приглашение $index: meetingId=${invitation.meetingId}, userId=${invitation.userId}, status=${invitation.responseStatus}")
        }
        invitations
    }

    suspend fun getInvitationsByMeetingId(meetingId: Long): Result<List<InvitationDto>> = withContext(Dispatchers.IO) {
        runCatching {
            // д э б
            println("DEBUG: Загружаем приглашения для встречи ID: $meetingId")
            val allInvitations = loadAllInvitations()
            // д э б
            val filtered = allInvitations.filter { it.meetingId == meetingId }
            println("DEBUG: Найдено ${filtered.size} приглашений для встречи $meetingId")
            filtered
        }
    }

    suspend fun getInvitations(userId: Int): Result<List<InvitationDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val list = Network.getInvitations()
            list.filter { it.userId == userId.toLong() }
        }
    }

    suspend fun getInvitationById(invitationId: Int): Result<InvitationDto> = withContext(Dispatchers.IO) {
        runCatching {
            val current = Network.client.get("/api/invitations/$invitationId")
            if (current.status != HttpStatusCode.OK) {
                error("Failed to get invitation: ${current.status}")
            }
            current.body<InvitationDto>()
        }
    }

    suspend fun updateInvitationResponse(
        invitationId: Int,
        responseStatus: String,
        comment: String? = null
    ): Result<InvitationDto> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = UpdateInvitationDto(
                responseStatus = responseStatus,
                responseComment = comment
            )

            val primary = Network.client.put("/api/invitations/$invitationId") {
                setBody(payload)
            }
            if (primary.status == HttpStatusCode.OK) {
                return@runCatching primary.body<InvitationDto>()
            }

            val alt1 = Network.client.put("/api/invitations/$invitationId/response") {
                setBody(payload)
            }
            if (alt1.status == HttpStatusCode.OK) {
                return@runCatching alt1.body<InvitationDto>()
            }


            val alt2 = Network.client.put("/api/invitations/$invitationId/decline") {
                setBody(payload)
            }
            if (alt2.status == HttpStatusCode.OK) {
                return@runCatching alt2.body<InvitationDto>()
            }

            error("Failed to update invitation: ${primary.status}")
        }
    }
}
