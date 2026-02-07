package ru.sicampus.bootcamp2026.domain.add

import ru.sicampus.bootcamp2026.data.EventRepository
import kotlin.Int

class CreateEventUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        organizerId: Int,
        title: String,
        description: String,
        date: String,
        startTime: String,
        endTime: String,
        participantsId: List<Int>
    ): Result<Unit>{
        return eventRepository.createEvent(organizerId,
            title,
            description,
            date,
            startTime,
            endTime,
            participantsId)
    }
}