package com.example.planify.main.features.auth.data.sources

import com.example.planify.main.features.auth.domain.schemas.AuthLocalInfoSchema
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    val authInfoFlow: Flow<AuthLocalInfoSchema?>

    suspend fun saveAuthInfo(info: AuthLocalInfoSchema)
    suspend fun clearAuthInfo()
}
