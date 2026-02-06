package ru.sicampus.bootcamp2026.domain.usecase.meeting

import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.model.MeetingStatus
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * Use case для получения списка встреч пользователя
 */
class GetUserMeetingsUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(
        status: MeetingStatus? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<PaginatedData<Meeting>> {
        return meetingRepository.getUserMeetings(status, page, size)
    }
}

