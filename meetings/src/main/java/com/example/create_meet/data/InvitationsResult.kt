package com.example.create_meet.data

sealed class InvitationsResult {
    data class Success(val invitations: List<InvitationResponse>): InvitationsResult()
    data class Error(val message: String): InvitationsResult()
}