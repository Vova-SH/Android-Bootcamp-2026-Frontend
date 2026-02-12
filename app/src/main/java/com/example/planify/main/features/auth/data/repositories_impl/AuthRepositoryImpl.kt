package com.example.planify.main.features.auth.data.repositories_impl

import com.example.planify.main.features.auth.data.sources.AuthLocalDataSource
import com.example.planify.main.features.auth.data.sources.AuthRemoteDataSource
import com.example.planify.main.features.auth.domain.entities.AuthContext
import com.example.planify.main.features.auth.domain.entities.AuthState
import com.example.planify.main.features.auth.domain.entities.AuthTokenPair
import com.example.planify.main.features.auth.domain.entities.LoginResult
import com.example.planify.main.features.auth.domain.repositories.AuthRepository
import com.example.planify.main.features.auth.domain.schemas.AuthLocalInfoSchema
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDatasource: AuthRemoteDataSource,
    private val localDatasource: AuthLocalDataSource
) : AuthRepository {
    private val _authStateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    override val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    override val localAuthInfoFlow: Flow<AuthLocalInfoSchema?> = localDatasource.authInfoFlow

    override suspend fun setAuthState(state: AuthState, syncLocal: Boolean) {
        if (syncLocal) {
            when (state) {
                is AuthState.Loading -> {}

                is AuthState.Authenticated -> {
                    saveLocalAuthInfo(
                        AuthLocalInfoSchema(
                            accessToken = state.tokenPair.accessToken,
                            refreshToken = state.tokenPair.refreshToken
                        )
                    )
                }

                is AuthState.Unauthenticated -> {
                    clearLocalAuthInfo()
                }
            }
        }

        _authStateFlow.value = state
    }

    override suspend fun register(shema: RegisterUserSchema): Result<LoginResult> {
        return remoteDatasource.register(shema)
    }

    override suspend fun login(email: String, password: String): Result<LoginResult> {
        return remoteDatasource.login(email, password)
    }

    override suspend fun refresh(refreshToken: String): Result<AuthTokenPair> {
        return remoteDatasource.refresh(refreshToken)
    }

    override suspend fun fetchActualAuthContext(accessToken: String): Result<AuthContext> {
        return remoteDatasource.fetchActualAuthContext(accessToken)
    }

    override suspend fun clearLocalAuthInfo() {
        localDatasource.clearAuthInfo()
    }

    override suspend fun saveLocalAuthInfo(schema: AuthLocalInfoSchema) {
        localDatasource.saveAuthInfo(info = schema)
    }

    override suspend fun localLogout() {
        localDatasource.clearAuthInfo()
        _authStateFlow.value = AuthState.Unauthenticated
    }
}
