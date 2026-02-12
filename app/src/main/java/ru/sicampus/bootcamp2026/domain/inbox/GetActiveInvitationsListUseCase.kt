package ru.sicampus.bootcamp2026.domain.inbox

import ru.sicampus.bootcamp2026.data.repository.InvitationRepository
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

class GetActiveInvitationsListUseCase(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(): Result<List<InvitationEntity>> {
        return repository.getActiveInvitations()
    }
}