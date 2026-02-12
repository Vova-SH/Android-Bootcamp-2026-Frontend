package com.example.create_meet.data

sealed interface InvitationResult {
    object Success : InvitationResult
    data class Error(val message: String) : InvitationResult
}