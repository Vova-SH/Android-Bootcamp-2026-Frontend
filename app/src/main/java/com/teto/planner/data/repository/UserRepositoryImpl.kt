package com.teto.planner.data.repository

import com.teto.planner.data.remote.dto.UsersPageDto
import com.teto.planner.data.remote.dto.toDomain
import com.teto.planner.data.remote.dto.user.UpdateUserRequest
import com.teto.planner.data.remote.dto.user.UserMeDto
import com.teto.planner.data.remote.dto.user.toDomain
import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.model.user.UserSummary
import com.teto.planner.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : UserRepository {

    override suspend fun getMe(): Result<UserMe> = runCatching {
        client.get("api/me")
            .body<UserMeDto>()
            .toDomain()
    }

    override suspend fun updateMe(name: String?, bio: String?, telegram: String?): Result<UserMe> =
        runCatching {
            val request = UpdateUserRequest(
                name = name,
                telegramNick = telegram,
                bio = bio
            )
            client.patch("api/me") {
                contentType(Json)
                setBody(request)
            }.body<UserMeDto>().toDomain()
        }

    override suspend fun listUsers(
        query: String?,
        page: Int,
        size: Int
    ): Result<PagedList<UserSummary>> = runCatching {
        val response = client.get("api/users") {
            if (!query.isNullOrBlank()) parameter("query", query)
            parameter("page", page)
            parameter("size", size)
            parameter("includeLoad", true)
        }.body<UsersPageDto>()

        PagedList(
            items = response.items.map { it.toDomain() },
            meta = response.meta?.toDomain() ?: PageMeta(page, size, 0)
        )
    }
}