package ru.sicampus.bootcamp2026.domain.usecase.invitation

import ru.sicampus.bootcamp2026.domain.model.Invitation
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
        invitationId: UUID,
        accept: Boolean
    ): Result<Invitation> {
        return invitationRepository.respondToInvitation(invitationId, accept)
    }
}

