package com.teto.planner.domain.repository

import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.model.user.UserSummary

interface UserRepository {
    suspend fun getMe(): Result<UserMe>

    suspend fun updateMe(name: String?, bio: String?, telegram: String?): Result<UserMe>

    suspend fun listUsers(
        query: String? = null,
        page: Int = 0,
        size: Int = 50
    ): Result<PagedList<UserSummary>>
}