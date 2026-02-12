package com.example.planify.main.features.auth.data.repositories_impl

import com.example.planify.main.features.auth.data.dto.get_active_sessions.GetActiveSessionsResponseDTO
import com.example.planify.main.features.auth.domain.entities.AuthSession
import com.example.planify.main.features.auth.domain.repositories.SessionsRepository
import com.example.planify.main.features.auth.domain.utils.network.AuthenticatedApiClient
import io.ktor.http.HttpMethod
import io.ktor.http.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionsRepositoryImpl @Inject constructor(
    private val authenticatedApiClient: AuthenticatedApiClient
) : SessionsRepository {
    private val authFeaturePath = "/auth"

    private val logoutPath = "$authFeaturePath/logout"
    private val getActiveSessionsPath = "$authFeaturePath/sessions/active"
    private val revokeSessionPath = "$authFeaturePath/sessions/%d"
    private val revokeAllSessionsExceptCurrentPath = "$authFeaturePath/sessions/active"

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Delete
                url { path(logoutPath) }
            }

            return@runCatching
        }
    }

    override suspend fun getActiveSessions(): Result<List<AuthSession>> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val responseDTO = authenticatedApiClient.requestNotNull<GetActiveSessionsResponseDTO> {
                method = HttpMethod.Get
                url { path(getActiveSessionsPath) }
            }

            responseDTO.sessions.map { it.toEntity() }
        }
    }

    override suspend fun revokeSession(sessionUuid: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Delete
                url { path(revokeSessionPath.format(sessionUuid)) }
            }

            return@runCatching
        }
    }

    override suspend fun revokeAllSessionsExceptCurrent(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Delete
                url { path(revokeAllSessionsExceptCurrentPath) }
            }

            return@runCatching
        }
    }
}
