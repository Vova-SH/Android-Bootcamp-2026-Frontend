package ru.sicampus.bootcamp2026.domain.mymeetings

import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity

class GetMeetingsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): Result<List<EventEntity>>{
        return eventRepository.getMeetings()
    }
}