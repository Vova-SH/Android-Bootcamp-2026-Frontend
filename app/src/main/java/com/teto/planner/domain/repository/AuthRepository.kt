package com.teto.planner.domain.repository

import com.teto.planner.domain.model.user.UserMe

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<UserMe>
    suspend fun register(name: String, login: String, password: String, telegramNick: String?): Result<UserMe>
    fun logout()
    fun isLoggedIn(): Boolean
}