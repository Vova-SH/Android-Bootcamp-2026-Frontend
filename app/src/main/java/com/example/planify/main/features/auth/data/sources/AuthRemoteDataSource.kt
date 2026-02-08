package com.example.planify.main.features.auth.data.sources

import com.example.planify.main.features.auth.domain.entities.AuthContext
import com.example.planify.main.features.auth.domain.entities.AuthTokenPair
import com.example.planify.main.features.auth.domain.entities.LoginResult
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema

interface AuthRemoteDataSource {
    suspend fun register(shema: RegisterUserSchema): Result<LoginResult>
    suspend fun login(email: String, password: String): Result<LoginResult>
    suspend fun refresh(refreshToken: String): Result<AuthTokenPair>
    suspend fun fetchActualAuthContext(accessToken: String): Result<AuthContext>
}
