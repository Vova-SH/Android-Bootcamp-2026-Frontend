package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class GetMeetingsUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(page: Int, size: Int): Result<List<Meeting>> {
        return repository.getMeetings(page, size)
    }
}