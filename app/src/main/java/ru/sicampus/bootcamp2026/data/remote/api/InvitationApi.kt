package ru.sicampus.bootcamp2026.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.sicampus.bootcamp2026.data.remote.dto.InvitationActionRequest
import ru.sicampus.bootcamp2026.data.remote.dto.InvitationResponse
import java.util.UUID

/**
 * API интерфейс для работы с приглашениями
 */
interface InvitationApi {

    @GET("api/v1/invitations")
    suspend fun getInvitations(): List<InvitationResponse>

    @GET("api/v1/invitations/{meetingId}")
    suspend fun getInvitationDetails(
        @Path("meetingId") meetingId: UUID
    ): InvitationResponse

    @PUT("api/v1/invitations/{meetingId}/respond")
    suspend fun respondToInvitation(
        @Path("meetingId") meetingId: UUID,
        @Body request: InvitationActionRequest
    ): InvitationResponse
}

