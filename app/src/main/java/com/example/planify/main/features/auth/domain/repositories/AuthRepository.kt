package com.example.planify.main.features.auth.domain.repositories

import com.example.planify.main.features.auth.domain.entities.AuthContext
import com.example.planify.main.features.auth.domain.entities.AuthState
import com.example.planify.main.features.auth.domain.entities.AuthTokenPair
import com.example.planify.main.features.auth.domain.entities.LoginResult
import com.example.planify.main.features.auth.domain.schemas.AuthLocalInfoSchema
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authStateFlow: StateFlow<AuthState>
    val localAuthInfoFlow: Flow<AuthLocalInfoSchema?>

    suspend fun register(shema: RegisterUserSchema): Result<LoginResult>
    suspend fun login(email: String, password: String): Result<LoginResult>
    suspend fun refresh(refreshToken: String): Result<AuthTokenPair>
    suspend fun fetchActualAuthContext(accessToken: String): Result<AuthContext>

    suspend fun clearLocalAuthInfo()
    suspend fun saveLocalAuthInfo(schema: AuthLocalInfoSchema)
    suspend fun localLogout()

    suspend fun setAuthState(state: AuthState, syncLocal: Boolean = true)
}
