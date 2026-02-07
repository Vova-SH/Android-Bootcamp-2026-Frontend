package com.example.meet.data.source

import com.example.meet.data.dto.InvitationDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime

@ExperimentalSerializationApi
class InvitationDataSource {

    suspend fun getInvitations(userId: Int): Result<List<InvitationDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val list = Network.getInvitations()
            list.filter { it.userId == userId.toLong() }
        }
    }

    suspend fun updateInvitationResponse(
        invitationId: Int,
        responseStatus: String,
        comment: String? = null
    ): Result<InvitationDto> = withContext(Dispatchers.IO) {
        runCatching {
            val current = Network.client.get("/api/invitations/$invitationId")
            if (current.status != HttpStatusCode.OK) {
                error("Failed to get invitation: ${current.status}")
            }
            val invitation = current.body<InvitationDto>()

            val updated = invitation.copy(
                responseStatus = responseStatus,
                responseComment = comment,
                respondedAt = LocalDateTime.now().toString(),
            )

            val response = Network.client.put("/api/invitations/$invitationId") { setBody(updated) }

            if (response.status != HttpStatusCode.OK) {
                error("Failed to update invitation: ${response.status}")
            }

            response.body<InvitationDto>()
        }
    }
}
