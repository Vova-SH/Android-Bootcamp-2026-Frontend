package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class UpdateMeetingUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(id: Long, title: String, desc: String, place: String, start: String, duration: Int): Result<Unit> =
        repository.updateMeeting(id, title, desc, place, start, duration)
}