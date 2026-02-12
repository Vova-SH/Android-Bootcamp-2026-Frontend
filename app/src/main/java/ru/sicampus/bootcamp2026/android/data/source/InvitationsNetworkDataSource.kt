package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import ru.sicampus.bootcamp2026.android.data.dto.InvitationRespondRequest
import ru.sicampus.bootcamp2026.android.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.android.data.dto.InvitationWithMeetingDto
import ru.sicampus.bootcamp2026.android.data.dto.PageResponse

class InvitationsNetworkDataSource {

    suspend fun getMyInvitationsWithMeeting(
        status: InvitationStatusDto,
        page: Int,
        size: Int
    ): Result<PageResponse<InvitationWithMeetingDto>> = runCatching {
        Network.client.get("${Network.HOST}/api/invitations/my/with-meeting") {
            addAuthHeader()
            parameter("status", status.name)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun respondInvitation(
        id: Long,
        status: String
    ): Result<Unit> = runCatching {
        Network.client.patch("${Network.HOST}/api/invitations/$id/respond") {
            addAuthHeader()
            setBody(InvitationRespondRequest(status))
        }.body()
    }
}
