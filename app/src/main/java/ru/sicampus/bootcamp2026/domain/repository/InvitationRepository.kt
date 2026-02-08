package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.domain.model.Invitation

interface InvitationRepository {
    suspend fun getInvitations(): Result<List<Invitation>>
    suspend fun sendInvitation(email: String, meetingId: Long): Result<Unit>
    suspend fun acceptInvitation(id: Long): Result<Unit>
    suspend fun rejectInvitation(id: Long): Result<Unit>
}