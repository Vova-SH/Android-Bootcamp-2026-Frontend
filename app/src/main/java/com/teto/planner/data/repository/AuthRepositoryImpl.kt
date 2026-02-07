package com.teto.planner.data.repository

import com.teto.planner.data.local.CredentialsHolder
import com.teto.planner.data.remote.dto.user.CreateUserRequest
import com.teto.planner.data.remote.dto.user.LoginRequest
import com.teto.planner.data.remote.dto.user.UserMeDto
import com.teto.planner.data.remote.dto.user.toDomain
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val credentialsHolder: CredentialsHolder
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<UserMe> = runCatching {
        val userDto = client.post("api/auth/login") {
            contentType(Json)
            setBody(LoginRequest(login, password))
        }.body<UserMeDto>()

        credentialsHolder.setCredentials(login, password)
        userDto.toDomain()
    }

    override suspend fun register(
        name: String,
        login: String,
        password: String,
        telegramNick: String?
    ): Result<UserMe> = runCatching {
        val user = client.post("api/auth/register") {
            contentType(Json)
            setBody(CreateUserRequest(login, name, password, telegramNick))
        }.body<UserMeDto>().toDomain()

        credentialsHolder.setCredentials(login, password)

        user
    }

    override fun logout() {
        credentialsHolder.clear()
    }

    override fun isLoggedIn(): Boolean {
        return credentialsHolder.hasCredentialsNow()
    }
}