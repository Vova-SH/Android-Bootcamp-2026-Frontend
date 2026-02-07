//package ru.sicampus.bootcamp2026.domain.usecase
//
//import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
//import java.time.LocalDate
//
//// srp не нарушается, но не очень, потом переорганизовать надо будет...
//class ViewScheduleUseCase(
//    private val meetingRepository: MeetingRepository
//) {
//    suspend fun getDaySchedule(day: LocalDate): Result<List<ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto>> {
//        return meetingRepository.getDaySchedule(day)
//    }
//
//    suspend fun getWeekSchedule(year: Int, week: Int): Result<List<ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto>> {
//        return meetingRepository.getWeekSchedule(year, week)
//    }
//
//    suspend fun getMonthSchedule(year: Int, month: Int): Result<List<ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto>> {
//        return meetingRepository.getMonthSchedule(year, month)
//    }
//}