package ru.sicampus.bootcamp2026.domain.usecase.meeting

import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.entities.MeetingMini
import java.time.LocalDate

class GetDayScheduleUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(day: LocalDate): Result<List<MeetingMini>> {
        return meetingRepository.getDaySchedule(day)
    }
}