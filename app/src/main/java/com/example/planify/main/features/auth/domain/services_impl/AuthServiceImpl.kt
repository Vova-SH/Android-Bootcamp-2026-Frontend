package com.example.planify.main.features.auth.domain.services_impl

import android.util.Log
import com.example.planify.core.exceptions.UnauthenticatedAppError
import com.example.planify.main.features.auth.domain.AuthTokenManager
import com.example.planify.main.features.auth.domain.entities.AuthContext
import com.example.planify.main.features.auth.domain.entities.AuthSession
import com.example.planify.main.features.auth.domain.entities.AuthState
import com.example.planify.main.features.auth.domain.entities.AuthTokenPair
import com.example.planify.main.features.auth.domain.entities.LoginResult
import com.example.planify.main.features.auth.domain.entities.UserPrivate
import com.example.planify.main.features.auth.domain.repositories.AuthRepository
import com.example.planify.main.features.auth.domain.repositories.SessionsRepository
import com.example.planify.main.features.auth.domain.repositories.UsersRepository
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema
import com.example.planify.main.features.auth.domain.services.AuthService
import jakarta.inject.Inject
import jakarta.inject.Provider
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex

@Singleton
class AuthServiceImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionsRepositoryProvider: Provider<SessionsRepository>,
    private val usersRepositoryProvider: Provider<UsersRepository>,
) : AuthService, AuthTokenManager {
    private val sessionsRepository get() = sessionsRepositoryProvider.get()
    private val usersRepository get() = usersRepositoryProvider.get()

    private val refreshMutex = Mutex()

    override val authStateFlow: StateFlow<AuthState> = authRepository.authStateFlow

    override fun isAuthenticated(): Boolean = authStateFlow.value is AuthState.Authenticated

    override suspend fun localLogout() {
        authRepository.localLogout()
    }

    override suspend fun readSavedAuthInfo() {
        val authInfo = authRepository.localAuthInfoFlow.first()

        if (authInfo == null) {
            authRepository.setAuthState(AuthState.Unauthenticated)
            return
        }

        try {
            var accessToken = authInfo.accessToken
            var refreshToken = authInfo.refreshToken
            var authContext = fetchActualAuthContext(accessToken)

            if (authContext.isFailure) {
                val error = authContext.exceptionOrNull()!!
                Log.w(this::class.simpleName, "Failed to fetch auth info using local tokens, refreshing: ${error::class.simpleName}: ${error.message}")

                val tokenPair = refreshUsingToken(refreshToken).getOrThrow()
                accessToken = tokenPair.accessToken
                refreshToken = tokenPair.refreshToken
                authContext = fetchActualAuthContext(accessToken)
            }

            authRepository.setAuthState(
                AuthState.Authenticated(
                    context = authContext.getOrThrow(),
                    tokenPair = AuthTokenPair(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )
            )
        } catch (error: Exception) {
            Log.w("Auth", "Failed to fetch auth info using local tokens: ${error::class.simpleName}: ${error.message}")
            localLogout()
        }
    }

    override suspend fun fetchActualAuthContext(accessToken: String): Result<AuthContext> {
        return authRepository.fetchActualAuthContext(accessToken)
    }

    override suspend fun register(shema: RegisterUserSchema): Result<LoginResult> {
        return authRepository.register(shema).onSuccess { result ->
            authRepository.setAuthState(
                AuthState.Authenticated(
                    context = result.authContext,
                    tokenPair = result.tokens
                )
            )
        }
    }

    override suspend fun login(email: String, password: String): Result<LoginResult> {
        return authRepository.login(email, password).onSuccess { result ->
            authRepository.setAuthState(
                AuthState.Authenticated(
                    context = result.authContext,
                    tokenPair = result.tokens
                )
            )
        }
    }

    private suspend fun refreshUsingToken(refreshToken: String): Result<AuthTokenPair> {
        return authRepository.refresh(refreshToken)
    }

    override suspend fun refresh(): Result<Unit> {
        val state = authStateFlow.value

        if (state !is AuthState.Authenticated) return Result.failure(
            UnauthenticatedAppError("Cannot refresh tokens: unauthorized")
        )

        if (!refreshMutex.tryLock()) {
            refreshMutex.lock()
            refreshMutex.unlock()

            return if (authStateFlow.value is AuthState.Authenticated) Result.success(Unit)
            else Result.failure(UnauthenticatedAppError("Failed to refresh tokens"))
        }

        try {
            return refreshUsingToken(state.tokenPair.refreshToken)
                .onSuccess { tokenPair ->
                    authRepository.setAuthState(
                        state.copy(
                            tokenPair = tokenPair
                        )
                    )
                }
                .onFailure { error ->
                    Log.w(this::class.simpleName, "Failed to refresh tokens: ${error::class.simpleName}: ${error.message}")
                    localLogout()
                }
                .map { }
        } finally {
            refreshMutex.unlock()
        }
    }

    override suspend fun logout(): Result<Unit> {
        return sessionsRepository.logout().onSuccess {
            localLogout()
        }
    }

    override suspend fun getActiveSessions(): Result<List<AuthSession>> {
        return sessionsRepository.getActiveSessions()
    }

    override suspend fun revokeSession(sessionUuid: String): Result<Unit> {
        return sessionsRepository.revokeSession(sessionUuid).onSuccess {
            if (
                authStateFlow.value is AuthState.Authenticated &&
                sessionUuid == (authStateFlow.value as AuthState.Authenticated).context.session.uuid
            ) localLogout()
        }
    }

    override suspend fun revokeAllSessionsExceptCurrent(): Result<Unit> {
        return sessionsRepository.revokeAllSessionsExceptCurrent()
    }

    override suspend fun fetchMe(): Result<UserPrivate> {
        return usersRepository.fetchMe()
    }

    override fun getTokenPair(): AuthTokenPair {
        if (authStateFlow.value !is AuthState.Authenticated) throw UnauthenticatedAppError("Cannot get auth tokens: Unauthenticated")
        return (authStateFlow.value as AuthState.Authenticated).tokenPair
    }

    override suspend fun refreshTokens(): Result<Unit> = refresh()
}
