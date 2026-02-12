package com.example.create_meet.domain.use_cases

import com.example.create_meet.data.dto.MeetingResponse
import com.example.create_meet.domain.MeetingRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveMeetingsUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    operator fun invoke(): StateFlow<List<MeetingResponse>> {
        return repository.meetings
    }
}


