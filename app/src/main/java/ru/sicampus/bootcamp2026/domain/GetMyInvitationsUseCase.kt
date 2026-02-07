package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

class GetMyInvitationsUseCase(
    private val meetingRepository: MeetingRepository = MeetingRepository()
) {
    suspend operator fun invoke(page: Int = 0, size: Int = 20): Result<List<InvitationEntity>> {
        return meetingRepository.getMyInvitations(page, size)
    }
}
