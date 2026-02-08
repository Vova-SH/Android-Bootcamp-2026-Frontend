package com.example.planify.main.features.auth.domain.services

import com.example.planify.main.features.auth.domain.entities.AuthContext
import com.example.planify.main.features.auth.domain.entities.AuthSession
import com.example.planify.main.features.auth.domain.entities.AuthState
import com.example.planify.main.features.auth.domain.entities.AuthTokenPair
import com.example.planify.main.features.auth.domain.entities.LoginResult
import com.example.planify.main.features.auth.domain.entities.UserPrivate
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema
import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    val authStateFlow: StateFlow<AuthState>

    suspend fun localLogout()

    fun isAuthenticated(): Boolean

    suspend fun register(shema: RegisterUserSchema): Result<LoginResult>
    suspend fun login(email: String, password: String): Result<LoginResult>
    suspend fun refresh(): Result<Unit>

    suspend fun logout(): Result<Unit>
    suspend fun getActiveSessions(): Result<List<AuthSession>>
    suspend fun revokeSession(sessionUuid: String): Result<Unit>
    suspend fun revokeAllSessionsExceptCurrent(): Result<Unit>

    suspend fun readSavedAuthInfo()
    suspend fun fetchActualAuthContext(accessToken: String): Result<AuthContext>

    suspend fun fetchMe(): Result<UserPrivate>
}
