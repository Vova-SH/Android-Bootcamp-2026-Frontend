package com.example.planify.main.features.auth.data.repositories_impl

import com.example.planify.main.features.auth.data.dto.get_me.GetMeResponseDTO
import com.example.planify.main.features.auth.domain.entities.UserPrivate
import com.example.planify.main.features.auth.domain.repositories.UsersRepository
import com.example.planify.main.features.auth.domain.utils.network.AuthenticatedApiClient
import io.ktor.http.HttpMethod
import io.ktor.http.path
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class UsersRepositoryImpl @Inject constructor(
    private val authenticatedApiClient: AuthenticatedApiClient
) : UsersRepository {
    override suspend fun fetchMe(): Result<UserPrivate> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<GetMeResponseDTO> {
                method = HttpMethod.Get
                url { path("/users/me") }
            }
            response.user.toEntity()
        }
    }
}
