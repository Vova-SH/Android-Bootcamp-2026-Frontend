package com.example.planify.main.features.auth.domain.repositories

import com.example.planify.main.features.auth.domain.entities.AuthSession

interface SessionsRepository {
    suspend fun logout(): Result<Unit>
    suspend fun getActiveSessions(): Result<List<AuthSession>>
    suspend fun revokeSession(sessionUuid: String): Result<Unit>
    suspend fun revokeAllSessionsExceptCurrent(): Result<Unit>
}
