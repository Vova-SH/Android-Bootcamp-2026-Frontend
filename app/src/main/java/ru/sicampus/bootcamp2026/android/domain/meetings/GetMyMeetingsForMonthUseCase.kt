package ru.sicampus.bootcamp2026.android.domain.meetings

import ru.sicampus.bootcamp2026.android.data.MeetingsRepository

class GetMyMeetingsForMonthUseCase(
    private val repository: MeetingsRepository
) {
    suspend operator fun invoke(month: String) = repository.getMyMeetingsForMonth(month)
}