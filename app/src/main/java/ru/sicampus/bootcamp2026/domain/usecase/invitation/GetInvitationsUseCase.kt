package ru.sicampus.bootcamp2026.domain.usecase.invitation

import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * Use case для получения списка приглашений
 */
class GetInvitationsUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(): Result<List<Invitation>> {
        return invitationRepository.getInvitations()
    }
}

