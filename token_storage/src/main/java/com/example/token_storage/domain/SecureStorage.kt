package com.example.token_storage.domain

interface SecureStorage {
    suspend fun getString(key: String): String?
    suspend fun putString(key: String, value: String)
    suspend fun clear()
}