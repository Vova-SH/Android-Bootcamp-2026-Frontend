package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository

class GetInvitationsUseCase(private val repository: InvitationRepository) {
    suspend operator fun invoke(): Result<List<Invitation>> = repository.getInvitations()
}

class SendInvitationUseCase(private val repository: InvitationRepository) {
    suspend operator fun invoke(email: String, meetingId: Long): Result<Unit> =
        repository.sendInvitation(email, meetingId)
}

class AcceptInvitationUseCase(private val repository: InvitationRepository) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.acceptInvitation(id)
}

class RejectInvitationUseCase(private val repository: InvitationRepository) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.rejectInvitation(id)
}