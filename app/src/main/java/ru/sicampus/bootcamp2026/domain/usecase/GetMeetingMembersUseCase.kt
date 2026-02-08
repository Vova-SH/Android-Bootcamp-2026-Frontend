package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.dto.MemberDto
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class GetMeetingMembersUseCase(private val repository: MeetingRepository) {
    suspend operator fun invoke(id: Long): Result<List<MemberDto>> = repository.getMeetingMembers(id)
}