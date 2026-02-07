package ru.sicampus.bootcamp2026.android.domain.meetings

import ru.sicampus.bootcamp2026.android.data.MeetingsRepository

class GetMyMeetingsForDayUseCase(
    private val repository: MeetingsRepository
) {

    suspend operator fun invoke(
        date: String,
        page: Int,
        size: Int
    ) = repository.getMyMeetingsForDay(date, page, size)
}
