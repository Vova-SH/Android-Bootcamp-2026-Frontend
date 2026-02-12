package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.CreateMeetingEntity

class CreateMeetingUseCase(
    private val repository: MeetingRepository
) {
    suspend operator fun invoke(
        meeting: CreateMeetingEntity,
        participants: List<String>
    ): Result<Unit> {
        return repository.createMeetingWithInvitations(meeting, participants)
    }
}