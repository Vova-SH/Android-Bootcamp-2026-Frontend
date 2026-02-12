package com.example.create_meet.domain.use_cases

import com.example.create_meet.domain.DomainResult
import com.example.create_meet.domain.MeetingRepository
import javax.inject.Inject

class DeclineInvitationUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    suspend operator fun invoke(invitationId: String): DomainResult<Unit> {
        return try {
            repository.acceptInvitation(invitationId)
            DomainResult.Success(Unit)
        } catch (e: Exception) {
            DomainResult.Error(e.message ?: "Не удалось подтвердить приглашение")
        }
    }
}