package com.example.create_meet.domain.use_cases

import com.example.create_meet.data.dto.CreateMeetingDto
import com.example.create_meet.data.dto.MeetingResponse
import com.example.create_meet.domain.MeetingRepository
import javax.inject.Inject

class CreateMeetingUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    suspend operator fun invoke(request: CreateMeetingDto): Result<MeetingResponse> =
        repository.createMeeting(request)
}