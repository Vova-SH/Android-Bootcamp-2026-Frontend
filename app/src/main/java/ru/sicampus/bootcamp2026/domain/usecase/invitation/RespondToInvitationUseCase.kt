package ru.sicampus.bootcamp2026.domain.usecase.invitation

import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.ParticipantStatus
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.util.UUID
import javax.inject.Inject

/**
 * Use case для ответа на приглашение
 */
class RespondToInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(
        meetingId: UUID,
        accept: Boolean
    ): Result<Invitation> {
        val status = if (accept) ParticipantStatus.CONFIRMED else ParticipantStatus.DECLINED
        return invitationRepository.respondToInvitation(meetingId, status)
    }
}

