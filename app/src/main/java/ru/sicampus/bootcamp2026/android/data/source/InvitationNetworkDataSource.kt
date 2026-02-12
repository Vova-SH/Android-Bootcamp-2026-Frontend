package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.android.data.dto.InvitationDTO
import ru.sicampus.bootcamp2026.android.data.dto.InvitationStatusDto

class InvitationNetworkDataSource {
    suspend fun createInvitation(
        meetingId: Long,
        inviteeId: Long
    ): Result<InvitationDTO> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("${Network.HOST}/api/invitations") {
                addAuthHeader()
                setBody(
                    CreateInvitationRequest(
                        meetingId = meetingId,
                        inviteeId = inviteeId,
                        status = InvitationStatusDto.PENDING,
                        respondedAt = null
                    )
                )
            }
            if (result.status == HttpStatusCode.OK || result.status == HttpStatusCode.Created) {
                result.body<InvitationDTO>()
            } else {
                error("Failed to create invitation: ${result.status}")
            }
        }
    }
}

@Serializable
data class CreateInvitationRequest(
    val meetingId: Long,
    val inviteeId: Long,
    val status: InvitationStatusDto,
    val respondedAt: String? = null
)