package ru.sicampus.bootcamp2026.android.domain.meetings

import ru.sicampus.bootcamp2026.android.data.MeetingsRepository

class GetMyMeetingsForWeekUseCase(
    private val repository: MeetingsRepository
) {
    suspend operator fun invoke(start: String) = repository.getMyMeetingsForWeek(start)
}