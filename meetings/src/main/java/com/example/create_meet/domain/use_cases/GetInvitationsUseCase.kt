package com.example.create_meet.domain.use_cases

import com.example.create_meet.data.InvitationResponse
import com.example.create_meet.domain.MeetingRepository
import javax.inject.Inject

class GetInvitationsUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    suspend operator fun invoke(): Result<List<InvitationResponse>> {
        return repository.getInvitations()
    }
}