package com.example.token_storage.domain

interface TokenRepository {
    suspend fun getToken(): String?
    suspend fun saveAccessToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clear()
}