package com.example.planify.main.features.auth.domain

import com.example.planify.main.features.auth.domain.entities.AuthTokenPair

interface AuthTokenManager {
    fun getTokenPair(): AuthTokenPair
    suspend fun refreshTokens(): Result<Unit>
}
