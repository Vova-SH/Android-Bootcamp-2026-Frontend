package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.TimeInfoDataSource
import ru.sicampus.bootcamp2026.domain.add.entities.TimeSlotEntity

class TimeRepository(
    private val timeInfoDataSource: TimeInfoDataSource
) {
    suspend fun getSlots(date: String): Result<List<TimeSlotEntity>>{
        return timeInfoDataSource.getSlots(date = date).mapCatching{ listDto ->
            listDto.mapNotNull { dto ->
                TimeSlotEntity(
                    date = dto.date ?: return@mapNotNull null,
                    startTime = dto.startTime ?: return@mapNotNull null,
                    endTime = dto.endTime ?: return@mapNotNull null,
                )
            }
        }
    }
}