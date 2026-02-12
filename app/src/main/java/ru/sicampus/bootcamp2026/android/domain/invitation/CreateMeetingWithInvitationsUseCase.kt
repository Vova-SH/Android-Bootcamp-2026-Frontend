package ru.sicampus.bootcamp2026.android.domain.invitation

import ru.sicampus.bootcamp2026.android.data.CreateMeetingRepository
import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse

class CreateMeetingWithInvitationsUseCase(
    private val repository: CreateMeetingRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        startAt: String,
        endAt: String,
        inviteeIds: List<Long>
    ): Result<MeetingResponse> {
        return repository.createMeetingWithInvitations(
            title = title,
            description = description,
            startAt = startAt,
            endAt = endAt,
            inviteeIds = inviteeIds
        )
    }
}