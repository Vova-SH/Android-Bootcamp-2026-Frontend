package ru.sicampus.bootcamp2026.domain.list

import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity

class GetInvitationsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): Result<List<EventEntity>>{
        return eventRepository.getInvitations()
    }
}