package ru.sicampus.bootcamp2026.android.domain.notifications_invite

import ru.sicampus.bootcamp2026.android.data.InvitationsRepository
import ru.sicampus.bootcamp2026.android.data.dto.InvitationStatusDto

class GetMyInvitesWithMeetingUseCase(
    private val repo: InvitationsRepository
) {
    suspend operator fun invoke(
        status: InvitationStatusDto,
        page: Int,
        size: Int
    ) = repo.getMyInvitationsWithMeeting(status, page, size)
}
