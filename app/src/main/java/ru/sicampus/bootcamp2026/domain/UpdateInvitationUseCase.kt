package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

class UpdateInvitationUseCase(
    private val meetingRepository: MeetingRepository = MeetingRepository()
) {
    suspend operator fun invoke(invitation: InvitationEntity): Result<InvitationEntity> {
        return meetingRepository.updateInvitation(invitation)
    }
}
