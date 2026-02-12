package ru.sicampus.bootcamp2026.android.domain.notifications_invite

import ru.sicampus.bootcamp2026.android.data.InvitationsRepository

class RespondInvitationUseCase(
    private val repo: InvitationsRepository
) {
    suspend operator fun invoke(id: Long, status: String) =
        repo.respondInvitation(id, status)
}
