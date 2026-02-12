package ru.sicampus.bootcamp2026.domain.usecase.invitation

import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.Invitation

class GetInvitationsUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): Result<List<Invitation>> {
        return meetingRepository.getInvitations()
    }
}