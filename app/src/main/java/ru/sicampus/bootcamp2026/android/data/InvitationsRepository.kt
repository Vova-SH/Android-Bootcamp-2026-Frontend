package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.android.data.dto.InvitationWithMeetingDto
import ru.sicampus.bootcamp2026.android.data.dto.PageResponse
import ru.sicampus.bootcamp2026.android.data.source.InvitationsNetworkDataSource

class InvitationsRepository(
    private val network: InvitationsNetworkDataSource
) {
    suspend fun getMyInvitationsWithMeeting(
        status: InvitationStatusDto,
        page: Int,
        size: Int
    ): Result<PageResponse<InvitationWithMeetingDto>> =
        network.getMyInvitationsWithMeeting(status, page, size)

    suspend fun respondInvitation(
        id: Long,
        status: String
    ): Result<Unit> = network.respondInvitation(id, status)
}
