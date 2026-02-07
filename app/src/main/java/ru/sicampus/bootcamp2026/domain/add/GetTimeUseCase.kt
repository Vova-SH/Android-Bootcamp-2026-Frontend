package ru.sicampus.bootcamp2026.domain.add

import ru.sicampus.bootcamp2026.data.TimeRepository
import ru.sicampus.bootcamp2026.domain.add.entities.TimeSlotEntity

class GetTimeUseCase(
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke( date: String): Result<List<TimeSlotEntity>>{
        return timeRepository.getSlots(date = date)
    }
}