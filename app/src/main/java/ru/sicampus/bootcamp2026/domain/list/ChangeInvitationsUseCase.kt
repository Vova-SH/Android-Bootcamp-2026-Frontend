package ru.sicampus.bootcamp2026.domain.list

import ru.sicampus.bootcamp2026.data.EventRepository

class ChangeInvitationsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        meetingId: Int,
        userId: Int,
        response: Boolean
    ): Result<Unit>{
        return eventRepository.changeInv(meetingId,userId,response)
    }
}