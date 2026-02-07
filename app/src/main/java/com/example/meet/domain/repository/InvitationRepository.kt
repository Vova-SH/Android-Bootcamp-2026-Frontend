package com.example.meet.domain.repository

import com.example.meet.domain.entity.Invitation

interface InvitationRepository {
    suspend fun getInvitations(userId: Int): Result<List<Invitation>>
    suspend fun updateResponse(invitationId: Int, responseStatus: String): Result<Invitation>
}