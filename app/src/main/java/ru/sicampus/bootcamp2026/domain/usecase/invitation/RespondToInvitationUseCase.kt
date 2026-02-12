package ru.sicampus.bootcamp2026.domain.usecase.invitation

import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.InvitationStatus

class RespondToInvitationUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(invitationId: Long, status: InvitationStatus): Result<Unit> {
        return meetingRepository.respondToInvitation(invitationId, status)
    }
}