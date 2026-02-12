package com.teto.planner.domain.repository

import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.model.user.UserSummary
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getMe(): Result<UserMe>

    suspend fun updateMe(name: String?, bio: String?, telegram: String?): Result<UserMe>

    suspend fun listUsers(
        query: String? = null,
        page: Int = 0,
        size: Int = 50
    ): Result<PagedList<UserSummary>>

    suspend fun uploadAvatar(bytes: ByteArray): Result<UserMe>

    fun getRecentUsers(): Flow<List<UserSummary>>

    suspend fun saveRecentUsers(users: List<UserSummary>)
}