package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.MeetingMini
import java.time.LocalDate

class ViewScheduleUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend fun getDaySchedule(day: LocalDate): Result<List<MeetingMini>> {
        return meetingRepository.getDaySchedule(day)
    }

//    suspend fun getWeekSchedule(year: Int, week: Int): Result<Map<LocalDate, List<MeetingMini>>> {
//        return meetingRepository.getWeekSchedule(year, week)
//    }
//
//    suspend fun getMonthSchedule(year: Int, month: Int): Result<Map<LocalDate, List<MeetingMini>>> {
//        return meetingRepository.getMonthSchedule(year, month)
//    }
}