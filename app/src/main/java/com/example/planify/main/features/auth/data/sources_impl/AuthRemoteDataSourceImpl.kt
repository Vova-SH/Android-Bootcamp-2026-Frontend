package com.example.planify.main.features.auth.data.sources_impl

import android.os.Build
import com.example.planify.main.common.network.api_client.ApiClient
import com.example.planify.main.features.auth.data.dto.get_actual_auth_context.GetActualAuthContextDTO
import com.example.planify.main.features.auth.data.dto.login.LoginRequestDTO
import com.example.planify.main.features.auth.data.dto.login.LoginResponseDTO
import com.example.planify.main.features.auth.data.dto.refresh.RefreshRequestDTO
import com.example.planify.main.features.auth.data.dto.refresh.RefreshResponseDTO
import com.example.planify.main.features.auth.data.dto.register.RegisterRequestDTO
import com.example.planify.main.features.auth.data.dto.register.RegisterResponseDTO
import com.example.planify.main.features.auth.data.sources.AuthRemoteDataSource
import com.example.planify.main.features.auth.domain.entities.AuthContext
import com.example.planify.main.features.auth.domain.entities.AuthTokenPair
import com.example.planify.main.features.auth.domain.entities.LoginResult
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.path
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val apiClient: ApiClient,
) : AuthRemoteDataSource {
    private val authFeaturePath = "/auth"

    private val registerPath = "$authFeaturePath/register"
    private val loginPath = "$authFeaturePath/login"
    private val refreshPath = "$authFeaturePath/refresh"
    private val fetchActualAuthContextPath = "$authFeaturePath/context"

    private fun getDefaultClientName(): String = "${Build.MANUFACTURER}-${Build.MODEL}"

    override suspend fun register(shema: RegisterUserSchema): Result<LoginResult> = withContext(Dispatchers.IO) {
        val requestDto = RegisterRequestDTO(
            firstName = shema.firstName,
            lastName = shema.lastName,
            username = shema.username,
            email = shema.email,
            password = shema.password,
            clientName = getDefaultClientName()
        )

        return@withContext runCatching {
            val responseDTO = apiClient.requestNotNull<RegisterResponseDTO> {
                method = HttpMethod.Post
                url { path(registerPath) }
                setBody(requestDto)
            }

            LoginResult(
                authContext = AuthContext(
                    session = responseDTO.session.toEntity(),
                    user = responseDTO.user.toEntity(),
                    accessInfo = responseDTO.accessInfo.toEntity()
                ),
                tokens = responseDTO.tokens.toEntity()
            )
        }
    }

    override suspend fun login(email: String, password: String): Result<LoginResult> = withContext(Dispatchers.IO) {
        val requestDto = LoginRequestDTO(
            email = email,
            password = password,
            clientName = getDefaultClientName()
        )

        return@withContext runCatching {
            val responseDTO = apiClient.requestNotNull<LoginResponseDTO> {
                method = HttpMethod.Post
                url { path(loginPath) }
                setBody(requestDto)
            }

            LoginResult(
                authContext = AuthContext(
                    session = responseDTO.session.toEntity(),
                    user = responseDTO.user.toEntity(),
                    accessInfo = responseDTO.accessInfo.toEntity()
                ),
                tokens = responseDTO.tokens.toEntity()
            )
        }
    }

    override suspend fun refresh(refreshToken: String): Result<AuthTokenPair> = withContext(Dispatchers.IO) {
        val requestDto = RefreshRequestDTO(
            refreshToken = refreshToken
        )

        return@withContext runCatching {
            val responseDTO = apiClient.requestNotNull<RefreshResponseDTO> {
                method = HttpMethod.Post
                url { path(refreshPath) }
                setBody(requestDto)
            }

            AuthTokenPair(
                accessToken = responseDTO.accessToken,
                refreshToken = responseDTO.refreshToken
            )
        }
    }

    override suspend fun fetchActualAuthContext(accessToken: String): Result<AuthContext> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = apiClient.requestNotNull<GetActualAuthContextDTO> {
                method = HttpMethod.Get
                url { path(fetchActualAuthContextPath) }
                headers {
                    set(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }

            response.context.toEntity()
        }
    }
}
