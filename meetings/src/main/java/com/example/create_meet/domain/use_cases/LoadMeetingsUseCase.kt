package com.example.create_meet.domain.use_cases

import com.example.create_meet.domain.MeetingRepository
import javax.inject.Inject

class LoadMeetingsUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    suspend operator fun invoke() {
        repository.getSchedule()
    }
}